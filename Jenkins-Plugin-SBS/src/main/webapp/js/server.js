/*
 * Initialize the data table.  Specifies a few options:
 * - sets the table length (number of rows) options to custom values
 * - sets a default order
 * - Save the state of a table (its paging position, ordering state etc) so that is can be restored when the user
 *   reloads a page, or comes back to the page after visiting a sub-page.
 *
 * More fun options here: https://datatables.net/examples/basic_init/
 */
$(document).ready((function () {

    serverEditor = new $.fn.dataTable.Editor({
        table: "#servers-table",
        idSrc:  'id',
        fields: [
            {
                label: "ID:",
                name:  "id"
            }, {
                label: "Host:",
                name:  "host"
            }, {
                label: "Port:",
                name:  "port"
            }, {
                label: "SHome:",
                name:  "shome"
            }, {
                label: "Username:",
                name:  "username"
            }, {
                label: "Password:",
                name:  "password"
            }
        ]
    });

    varServicesTable = $('#server-var-services-table').DataTable({
        rowId: 'id',
        order: [],
        dom: 't',
        // retrieve: true,
        columns: [
            {data: "id"},
            {data: "dir"},
            {data: "port"},
            {data: "dPort"},
            {data: "arg"}
        ]
    });

    runServicesTable = $('#server-run-services-table').DataTable({
        rowId: 'pid',
        order: [],
        dom: 't',
        // retrieve: true,
        columns: [
            {data: "pid"},
            {data: "sid"},
            {data: "detail"},
            {
                data: null,
                className: "center",
                defaultContent: '<div class="oper"><b><span id="kill">kill</span></b></div>'
            }
        ],
        rowCallback: function(row, data) {
            $('.oper #kill', row).off("click").css('color', 'red').attr('title', "kill this service.");
            if (hasPermission) {
                $('.oper #kill', row).click(function() {
                    serverController.kill($('#envSelect').val(), data.server, data.pid, function(resp) {
                        $('.oper #kill', row).html("killed").off("click");
                    });
                });
            } else {
                $('.oper #kill', row).css('color', '#C87272').attr('title', "do not have permissions.");
            }
        }
    });


    serversTable = $('#servers-table').DataTable({
        // lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
        // order: [[0, "asc"]],
        rowId: 'id',
        order: [],
        dom: '<"toolbar">ftrip',
        columns: [
            { data: "id" },
            { data: "host" },
            { data: "port" },
            { data: "shome" },
            { data: "username" },
            { data: "password" },
            {
                data: null,
                className: "center",
                defaultContent: '<div class="oper">loading...</div>'
            }
        ],
        rowCallback: function(row, data) {
            serverController.valid($('#envSelect').val(), data.id, function(resp) {
                var responseObject = resp.responseObject();
                if (responseObject === 'success') {
                    $('.oper', row).html('<b><span id="services">services</span> / <span onClick="loginLogs(\''+data.id+'\')">login-logs</span> / <span onClick="operLogs(\''+data.id+'\')">oper-logs</span></b>');
                    serviceLoad($('.oper #services', row), data.id);
                } else {
                    $('.oper', row).html('<b title="'+responseObject+'">'+responseObject+'</b>');
                }
            });
        }
    });

    serviceLoad = function(dom, sid) {
        dom.attr('title', "service info loading...").css("color", "black").off("click");
        serverController.services($('#envSelect').val(), sid, function(resp) {
            var responseObject = resp.responseObject();
            if (0 !== responseObject.exitCode) {
                dom.attr('title', responseObject.err);
            } else {
                var skey = $('#envSelect').val() + '-' + sid;
                this.services[skey] = [];
                (responseObject.out || '').split('\n').each(function(service) {
                    if (!service || !service.trim() || service.indexOf('sun.tools.jps.Jps') != -1) return;
                    var pid = service.substring(0, service.indexOf(' '));
                    var detail = service.substring(service.indexOf(' ') + 1);
                    var pname = detail.match('/home/.*/(.*?)\.jar');
                    this.services[skey].push({pid: pid, server: sid, detail: detail, sid: pname && pname.length > 1 ? pname[1] : null});
                });
                var required = this.envServices.filter(function(service) {
                    return service.sid == sid;
                });
                dom.attr('title', "show services detail.").css("color", required.length == this.services[skey].length ? "green" : "#F48024").click(function() {
                    event.cancelBubble = true;

                    varServicesTable.clear();
                    varServicesTable.rows.add(required);


                    runServicesTable.clear();
                    runServicesTable.rows.add(services[skey]);

                    serviceDialog.create({
                        title: 'Services Details'
                    });
                    serviceDialog.on('close', function() {
                        serviceLoad(dom, sid);
                        serviceDialog.off('close');
                    });
                    varServicesTable.draw();
                    runServicesTable.draw();
                });
            }
        });
    }

    loginLogs = function(id) {
        event.cancelBubble = true;
        serverController.loginLogs($('#envSelect').val(), id, function(resp) {
            $('#serverDialog').html(resp.responseObject().out);
        });
        serverDialog.create({
            title: 'Login Logs'
        });
        $('#serverDialog').html('loading...');
    }

    operLogs = function(id) {
        event.cancelBubble = true;
        serverController.operLogs($('#envSelect').val(), id, function(resp) {
            $('#serverDialog').html(resp.responseObject().out);
        });
        serverDialog.create({
            title: 'Oper Logs'
        });
        $('#serverDialog').html('loading...');
    }

    $("#servers-table-div .toolbar").html('<b>env: </b><select id="envSelect"></select>');

    $('#envSelect').change(function() {
        loadServers($(this).val());
    });

    $('#servers-table tbody').on('click', 'tr', function (e) {
        e.preventDefault();
        if (serversTable.row(this).id()) {
            saveServer('Edit record', $(this));
        }
    });

    loadServers = function(envName) {
        serverController.getByEnv(envName, function(resp) {
            var responseObject = resp.responseObject();
            serversTable.clear();
            $('#serverSelect').empty();
            $('#serverSelect').append('<option value="">All</option>');
            var options = [];
            if (responseObject && responseObject.length > 0) {
                serversTable.rows.add(responseObject);
                responseObject.each(function(obj) {
                    $('#serverSelect').append('<option value="' + obj.id + '">' + obj.id + '</option>');
                    options.push({label: obj.id, value: obj.id});
                });
                // loadServices(envName, responseObject[0].id);
            }
            loadServices(envName);
            serviceEditor.field('sid').update(options);
            serversTable.draw();
        });
    }

    saveServer = function(title, data) {
        var currentEnv = $('#envSelect').val();
        if (!currentEnv) {
            alert('Please select a valid <env> first');
            return false;
        }
        var cb = function(resp) {
            loadServers(currentEnv);
            this.close();
        };
        var json = {
            title: title,
            buttons: []
        };
        if (hasPermission) {
            json.buttons.push({
                label: "Save",
                fn: function () {
                    serverController.add(currentEnv, this.get('id'), this.get('host'), this.get('port'), this.get('shome'), this.get('username'), this.get('password'), null, cb.bind(this));
                }
            });
        }
        if (data) {
            if (hasPermission) {
                json.buttons.push({
                    label: "Delete",
                    fn: function () {
                        serverController.del(currentEnv, this.get('id'), cb.bind(this));
                    }
                });
            }
            serverEditor.field('id').disable();
            serverEditor.edit(data, json);
        } else {
            serverEditor.field('id').enable();
            serverEditor.create(json);
        }
    }

    addServer = function() {
        saveServer('Add record');
    }

    // loadServers();




    serverDialog = new $.fn.dataTable.Editor({
        template: '#serverDialog'
    });
    serverDialog.buttons([{
        label: "Close",
        fn: function () {
            this.close();
        }
    }]);

    serviceDialog = new $.fn.dataTable.Editor({
        template: '#serviceDialog'
    });
    serviceDialog.buttons([{
        label: "Close",
        fn: function () {
            this.close();
        }
    }]);

}).bind(this));