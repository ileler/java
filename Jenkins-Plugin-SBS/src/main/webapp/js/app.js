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

    //<<<Service Start

    serviceEditor = new $.fn.dataTable.Editor({
        table: "#services-table",
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

    servicesTable = $('#services-table').DataTable({
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
        order: [[0, "asc"]],
        columns: [
            { data: "name" },
            { data: "path" },
            { data: "template" }
        ]
    });

    $('#services-table tbody').on('click', 'tr', function (e) {
        e.preventDefault();
        saveEnv('Edit record', $(this));
    });

    loadServices = function(envName) {
        controller.envs(function(resp) {
            servicesTable.clear().rows.add(resp.responseObject()).draw();
        });
    }

    saveService = function(title, data) {
        var cb = function(resp) {
            loadServices();
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
            serviceEditor.field('name').disable();
            serviceEditor.edit(data, json);
        } else {
            serviceEditor.field('name').enable();
            serviceEditor.create(json);
        }
    }

    addService = function() {
        saveService('Add record');
    }

    // loadService();

    //>>>Service End



    //<<<Server Start

    serverEditor = new $.fn.dataTable.Editor({
        table: "#servers-table",
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

    serversTable = $('#servers-table').DataTable({
        lengthMenu: [[10, 25, 50, -1], [10, 25, 50, "All"]],
        order: [[0, "asc"]],
        columns: [
            { data: "name" },
            { data: "path" },
            { data: "template" }
        ]
    });

    $('#servers-table tbody').on('click', 'tr', function (e) {
        e.preventDefault();
        saveEnv('Edit record', $(this));
    });

    loadServers = function(envName) {
        controller.envs(function(resp) {
            serversTable.clear().rows.add(resp.responseObject()).draw();
        });
    }

    saveServer = function(title, data) {
        var cb = function(resp) {
            loadServers();
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
            serverEditor.field('name').disable();
            serverEditor.edit(data, json);
        } else {
            serverEditor.field('name').enable();
            serverEditor.create(json);
        }
    }

    addServer = function() {
        saveServer('Add record');
    }

    // loadServers();

    //>>>Server End



    //<<<Env Start

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

    //>>>Env End

}).bind(this));