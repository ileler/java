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

    servicesTable = $('#services-table').DataTable({
        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "order": [[0, "asc"]],
        stateSave: true,
        "columnDefs": [{
            "targets": 4,
            "render": function (data, type, row, meta) {
                return data.replace(new RegExp(',', 'g'), ' ');
            }
        }]
    });

    serversTable = $('#servers-table').DataTable({
        "lengthMenu": [[10, 25, 50, -1], [10, 25, 50, "All"]],
        "order": [[0, "asc"]],
        stateSave: true,
        "columnDefs": [{
            "targets": 4,
            "render": function (data, type, row, meta) {
                return data.replace(new RegExp(',', 'g'), ' ');
            }
        }]
    });

    envEditor = new $.fn.dataTable.Editor({
        table: "#envs-table",
        idSrc:  'name',
        fields: [
            {
                label: "Name:",
                name:  "name"
            }, {
                label: "Path:",
                name:  "path"
            }, {
                label:     "Template:",
                fieldInfo: "Value can be template text or template file path",
                name:      "template",
                type:      "textarea"
            }
        ]
    });

    envsTable = $('#envs-table').DataTable({
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
        order: [[0, "asc"]],
        searching: false,
        columns: [
            { data: "name" },
            { data: "path" },
            { data: "template" }
        ]
    });

    $('#envs-table tbody').on('click', 'tr', function (e) {
        e.preventDefault();
        saveEnv('Edit record', $(this));
    });



    loadEnvs = function() {
        controller.envs(function(resp) {
            envsTable.clear().rows.add(resp.responseObject()).draw();
        });
    }

    saveEnv = function(title, data) {
        var cb = function(resp) {
            loadEnvs();
            this.close();
        };
        var json = {
            title: title,
            buttons: [{
                label: "Save",
                fn: function () {
                    controller.addEnv(this.get('name'), this.get('path'), this.get('template'), cb.bind(this));
                }
            }]
        };
        if (data) {
            if (hasPermission) {
                json.buttons.push({
                    label: "Delete",
                    fn: function () {
                        controller.delEnv(this.get('name'), cb.bind(this));
                    }
                });
                json.buttons.push({
                    label: "Reset",
                    fn: function () {
                        controller.resetEnv(this.get('name'), cb.bind(this));
                    }
                });
            }
            envEditor.field('name').disable();
            envEditor.edit(data, json);
        } else {
            envEditor.field('name').enable();
            envEditor.create(json);
        }
    }

    addEnv = function() {
        saveEnv('Add record');
    }

    loadEnvs();

}).bind(this));