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

    serviceEditor = new $.fn.dataTable.Editor({
        table: "#services-table",
        idSrc:  'id',
        fields: [
            {
                label: "ID:",
                name:  "id"
            }, {
                label: "SID:",
                name:  "sid",
                type:  "select"
            }, {
                label: "Dir:",
                name:  "dir"
            }, {
                label: "Port:",
                name:  "port"
            }, {
                label: "DPort:",
                name:  "dPort"
            }, {
                label: "Arg:",
                name:  "arg"
            }
        ]
    });

    servicesTable = $('#services-table').DataTable({
        // lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
        // order: [[0, "asc"]],
        rowId: 'id',
        order: [],
        dom: '<"toolbar">ftrip',
        columns: [
            { data: "id" },
            { data: "sid" },
            { data: "dir" },
            { data: "port" },
            { data: "dPort" },
            { data: "arg" },
        ]
    });

    $("#services-table-div .toolbar").html('<b>server: </b><select id="serverSelect"></select>');

    $('#serverSelect').change(function() {
        loadServices($('#envSelect').val(), $(this).val());
    });

    $('#services-table tbody').on('click', 'tr', function (e) {
        e.preventDefault();
        if (servicesTable.row(this).id()) {
            saveService('Edit record', $(this));
        }
    });

    loadServices = function(currentEnv, currentserver) {
        profileController.getBySID(currentEnv, currentserver || null, function(resp) {
            var responseObject = resp.responseObject();
            servicesTable.clear();
            if (responseObject) {
                servicesTable.rows.add(responseObject);
            }
            servicesTable.draw();
        });
    }

    saveService = function(title, data) {
        var currentEnv = $('#envSelect').val();
        var currentserver = $('#serverSelect').val();
        var cb = function(resp) {
            loadServices(currentEnv, currentserver);
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
                    var sid = this.get('sid');
                    if (!sid) {
                        alert('Please select a valid <sid> first');
                        return false;
                    }
                    profileController.add(currentEnv, this.get('id'), sid, this.get('dir'), this.get('arg'), this.get('port'), this.get('dPort'), cb.bind(this));
                }
            });
        }
        if (data) {
            if (hasPermission) {
                json.buttons.push({
                    label: "Delete",
                    fn: function () {
                        profileController.del(currentEnv, this.get('id'), cb.bind(this));
                    }
                });
            }
            serviceEditor.field('id').disable();
            serviceEditor.edit(data, json);
        } else {
            serviceEditor.field('id').enable();
            serviceEditor.field('sid').def(currentserver);
            serviceEditor.create(json);
        }
    }

    addService = function() {
        saveService('Add record');
    }

    // loadService();

}).bind(this));