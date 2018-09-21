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
                    $('.oper', row).html('<b><span onClick="loginLogs('+data.id+')">login-logs</span> / <span onClick="operLogs('+data.id+')">oper-logs</span></b>');
                } else {
                    $('.oper', row).html('<b title="'+responseObject+'">'+responseObject+'</b>');
                }
            });
        }
    });

    loginLogs = function(id) {
        event.cancelBubble = true;
        serverController.loginLogs($('#envSelect').val(), id, function(resp) {
            alert(resp.responseObject().out);
        });
    }

    operLogs = function(id) {
        event.cancelBubble = true;
        serverController.operLogs($('#envSelect').val(), id, function(resp) {
            alert(resp.responseObject().out);
        });
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
            var options = [];
            if (responseObject && responseObject.length > 0) {
                serversTable.rows.add(responseObject);
                responseObject.each(function(obj) {
                    $('#serverSelect').append('<option value="' + obj.id + '">' + obj.id + '</option>');
                    options.push({label: obj.id, value: obj.id});
                });
                loadServices(envName, responseObject[0].id);
            }
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

}).bind(this));