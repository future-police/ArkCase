/**
 * Complaint.Page
 *
 * manages all dynamic created page element
 *
 * @author jwu
 */
Complaint.Page = {
    create : function() {
    }

    ,URL_TASK_DETAIL: "/plugin/task/"
    ,URL_NEW_TASK:    "/plugin/task/wizard?parentType=COMPLAINT&reference="




    ,fillReportSelection: function() {
        var html = "<span>"
            + "<select class='input-sm form-control input-s-sm inline v-middle'>"
            + "<option value=''>Document Type</option>"
            + "<option value='electronic_communication'>Electronic Communication</option>"
            + "<option value='roi'>Report of Investigation</option>"
            + "</select>"
            + "</span>";
        Complaint.Object.beforeSpanAddDocument(html);
    }

};

