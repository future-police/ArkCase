/**
 * CaseFileSplit.View
 *
 * @author md
 */
CaseFileSplit.View = CaseFileSplit.View || {
    create : function() {
        if (CaseFileSplit.View.MicroData.create)       {CaseFileSplit.View.MicroData.create();}
        if (CaseFileSplit.View.BootstrapWizard.create)       {CaseFileSplit.View.BootstrapWizard.create();}
        if (CaseFileSplit.View.Detail.create)          {CaseFileSplit.View.Detail.create();}
        //if (CaseFileSplit.View.People.create)    	  {CaseFileSplit.View.People.create();}
        if (CaseFileSplit.View.Documents.create)       {CaseFileSplit.View.Documents.create();}
        //if (CaseFileSplit.View.Participants.create)    {CaseFileSplit.View.Participants.create();}
        //if (CaseFileSplit.View.Notes.create)           {CaseFileSplit.View.Notes.create();}
        if (CaseFileSplit.View.Summary.create)           {CaseFileSplit.View.Summary.create();}
    }
    ,onInitialized: function() {
        if (CaseFileSplit.View.MicroData.onInitialized)       {CaseFileSplit.View.MicroData.onInitialized();}
        if (CaseFileSplit.View.BootstrapWizard.onInitialized)          {CaseFileSplit.View.BootstrapWizard.onInitialized();}
        if (CaseFileSplit.View.Detail.onInitialized)          {CaseFileSplit.View.Detail.onInitialized();}
        //if (CaseFileSplit.View.People.onInitialized)          {CaseFileSplit.View.People.onInitialized();}
        if (CaseFileSplit.View.Documents.onInitialized)       {CaseFileSplit.View.Documents.onInitialized();}
        //if (CaseFileSplit.View.Participants.onInitialized)    {CaseFileSplit.View.Participants.onInitialized();}
        //if (CaseFileSplit.View.Notes.onInitialized)           {CaseFileSplit.View.Notes.onInitialized();}
        if (CaseFileSplit.View.Summary.onInitialized)         {CaseFileSplit.View.Summary.onInitialized();}
    }


    ,MicroData: {
        create : function() {
            this.parentCaseFileId      = Acm.Object.MicroData.get("parentCasefileId");
            this.fileTypes             = Acm.Object.MicroData.getJson("fileTypes");
            this.arkcaseUrl            = Acm.Object.MicroData.get("arkcaseUrl");
            this.arkcasePort           = Acm.Object.MicroData.get("arkcasePort");
        }
        ,onInitialized: function() {
        }
    }

    ,BootstrapWizard: {
        create: function() {
            this.bootstrapWizardForm = $("#wizardform");
            this.tabPanes            = $(".tab-pane");
            this.loadBootStrapWizard(this.bootstrapWizardForm);
        }
        ,onInitialized: function() {
        }
        ,loadBootStrapWizard: function($s){
                $s.bootstrapWizard({
                'tabClass': 'nav nav-tabs',
                'onNext': function(tab, navigation, index) {
                    return true;
                },
                onTabClick: function(tab, navigation, index) {
                    return true;
                },
                onTabShow: function(tab, navigation, index) {
                    var $total = navigation.find('li').length;
                    var $current = index+1;
                    if($total == $current){
                        CaseFileSplit.View.BootstrapWizard.tabPanes.show();
                    }
                    else{
                        CaseFileSplit.View.BootstrapWizard.tabPanes.hide();
                        var activeTab = navigation.find('li.active a').attr('href');
                        $(activeTab).show();
                    }
                }
            });
        }
    }


    ,Detail: {
        create: function() {
            this.$divDetail       = $(".divDetail");
            this.$btnEditDetail   = $("#tabDetail button:eq(0)");
            this.$btnSaveDetail   = $("#tabDetail button:eq(1)");
            this.$btnEditDetail.on("click", function(e) {CaseFileSplit.View.Detail.onClickBtnEditDetail(e, this);});
            this.$btnSaveDetail.on("click", function(e) {CaseFileSplit.View.Detail.onClickBtnSaveDetail(e, this);});

            this.$txtOriginalCaseId = $("#originalCaseID");
            this.$txtCaseTitle      = $("#caseTitle");
            this.$txtIncidentDate   = $("#incident");
            this.$txtPriority       = $("#priority");
            this.$txtAssignee       = $("#assigned");
            this.$txtGroup 		    = $("#group");
            this.$txtSubjectType    = $("#type");
            this.$txtDueDate        = $("#dueDate");
            this.$txtStatus         = $("#status");

            Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_RETRIEVED_PARENT_CASE_FILE             ,this.onModelRetrievedParentCaseFile);
            Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_SAVED_DETAIL                           ,this.onModelSavedDetail);
        }
        ,onInitialized: function() {
        }

        ,DIRTY_EDITING_DETAIL: "Editing case detail"
        ,onClickBtnEditDetail: function(event, ctrl) {
            App.View.Dirty.declare(CaseFileSplit.View.Detail.DIRTY_EDITING_DETAIL);
            CaseFileSplit.View.Detail.editDivDetail();
        }
        ,onClickBtnSaveDetail: function(event, ctrl) {
            var htmlDetail = CaseFileSplit.View.Detail.saveDivDetail();
            CaseFileSplit.Controller.viewChangedDetail(CaseFileSplit.View.getActiveCaseFileId(), htmlDetail);
            App.View.Dirty.clear(CaseFileSplit.View.Detail.DIRTY_EDITING_DETAIL);
        }
        ,onModelRetrievedParentCaseFile: function(parentCaseFile){
            CaseFileSplit.View.Detail.populateCaseFile(parentCaseFile);
        }

        ,populateCaseFile: function(c) {
            if (CaseFileSplit.Model.Detail.validateCaseFile(c)) {
                this.setOriginalCaseId(Acm.goodValue(c.id));
                this.setCaseTitle(Acm.goodValue(c.title));
                //this.setIncidentDate(Acm.getDateFromDatetime(c.created));//c.incidentDate
                this.setIncidentDate(Acm.getDateFromDatetime2(c.created,$.t("common:date.short")));
                this.setSubjectType(Acm.goodValue(c.caseType));
                this.setPriority(Acm.goodValue(c.priority));
                //this.setDueDate(Acm.getDateFromDatetime(c.dueDate));
                this.setDueDate(Acm.getDateFromDatetime2(c.dueDate,$.t("common:date.short")));
                this.setHtmlDivDetail(Acm.goodValue(c.details));

                var assignee = CaseFileSplit.Model.Detail.getAssignee(c);
                this.setAssignee(Acm.goodValue(assignee));

                var group = CaseFileSplit.Model.Detail.getGroup(c);
                this.setGroup(Acm.goodValue(group));
            }
        }

        ,setOriginalCaseId: function(txt) {
            Acm.Object.setText(this.$txtOriginalCaseId, txt);
        }
        ,getOriginalCaseId: function() {
            return Acm.Object.getText(this.$txtOriginalCaseId);
        }
        ,setCaseTitle: function(txt) {
                Acm.Object.setText(this.$txtCaseTitle, txt);
        }
        ,getCaseTitle: function() {
            return Acm.Object.getText(this.$txtCaseTitle);
        }
        ,setIncidentDate: function(txt) {
            Acm.Object.setText(this.$txtIncidentDate, txt);
        }
        ,getIncidentDate: function() {
            return Acm.Object.getText(this.$txtIncidentDate);
        }
        ,setAssignee: function(txt) {
            Acm.Object.setText(this.$txtAssignee, txt);
        }
        ,getAssignee: function() {
            return Acm.Object.getText(this.$txtAssignee);
        }
        ,setGroup: function(txt) {
            Acm.Object.setText(this.$txtGroup, txt);
        }
        ,getGroup: function() {
            return Acm.Object.getText(this.$txtGroup);
        }
        ,setSubjectType: function(txt) {
            Acm.Object.setText(this.$txtSubjectType, txt);
        }
        ,getSubjectType: function() {
            return Acm.Object.getText(this.$txtSubjectType);
        }
        ,setPriority: function(txt) {
            Acm.Object.setText(this.$txtPriority, txt);
        }
        ,getPriority: function() {
            return Acm.Object.getText(this.$txtPriority);
        }
        ,setDueDate: function(txt) {
            Acm.Object.setText(this.$txtDueDate, txt);
        }
        ,getDueDate: function() {
            return Acm.Object.getText(this.$txtDueDate);
        }
        ,getHtmlDivDetail: function() {
            return AcmEx.Object.SummerNote.get(this.$divDetail);
        }
        ,setHtmlDivDetail: function(html) {
            AcmEx.Object.SummerNote.set(this.$divDetail, html);
        }
        ,editDivDetail: function() {
            AcmEx.Object.SummerNote.edit(this.$divDetail);
        }
        ,saveDivDetail: function() {
            return AcmEx.Object.SummerNote.save(this.$divDetail);
        }
    }
    
    ,People: {
        create: function() {
            this.$divPeople = $("#divPeople");
            this.createJTable(this.$divPeople);
            Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_RETRIEVED_PARENT_CASE_FILE             ,this.onModelRetrievedParentCaseFile);
        }
        ,onInitialized: function() {
        }
        ,onModelRetrievedParentCaseFile: function(parentCaseFile){
            if (parentCaseFile.hasError) {
                Acm.View.MessageBoard("Error retrieving casefile", parentCaseFile.errorMsg);
            }
            else{
                AcmEx.Object.JTable.load(CaseFileSplit.View.People.$divPeople);
            }
        }
        ,createJTable: function($s) {
            AcmEx.Object.JTable.useBasic($s
                ,{
                    title: $.t("casefile:people.table.title")
                    ,paging: true
                    ,sorting: true
                    ,pageSize: 10 //Set page size (default: 10)
                    ,actions: {
                        listAction: function(postData, jtParams) {
                            var rc = AcmEx.Object.JTable.getEmptyRecords();
                            var c = CaseFileSplit.Model.Detail.cacheParentCaseFile.get(CaseFileSplit.View.MicroData.parentCaseFileId);
                            if (CaseFileSplit.Model.Detail.validateCaseFile(c)) {
                                var personAssociations = c.personAssociations;
                                for (var i = 0; i < personAssociations.length; i++) {
                                    if (CaseFileSplit.Model.People.validatePersonAssociation(personAssociations[i])) {
                                        rc.Records.push({
                                            assocId:     personAssociations[i].id
                                            ,title:      personAssociations[i].person.title
                                            ,givenName:  personAssociations[i].person.givenName
                                            ,familyName: personAssociations[i].person.familyName
                                            ,personType: personAssociations[i].personType
                                        });
                                    }
                                }
                                rc.TotalRecordCount = rc.Records.length;
                            }
                            return rc;
                        }
                    }
                    ,fields: {
                        assocId: {
                            title: $.t("casefile:people.table.field.id")
                            ,key: true
                            ,list: false
                            ,create: false
                            ,edit: false
                        }
                        ,title: {
                            title: $.t("casefile:people.table.field.title")
                            ,width: '10%'
                            ,options: CaseFileSplit.Model.Lookup.getPersonTitles()
                        }
                        ,givenName: {
                            title: $.t("casefile:people.table.field.first-name")
                            ,width: '15%'
                        }
                        ,familyName: {
                            title: $.t("casefile:people.table.field.last-name")
                            ,width: '15%'
                        }
                        ,personType: {
                            title: $.t("casefile:people.table.field.type")
                            ,options: CaseFileSplit.Model.Lookup.getPersonTypes()
                        }
                    }
                }
            );
        }
    }


    ,Documents: {
            create: function() {
                Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_RETRIEVED_PARENT_CASE_FILE             ,this.onModelRetrievedParentCaseFile);
            }
            ,onInitialized: function() {
            }
            ,onModelRetrievedParentCaseFile: function(parentCaseFile){
                if (parentCaseFile.hasError) {
                    Acm.View.MessageBoard("Error retrieving casefile" ,parentCaseFile.errorMsg);
                }
                else{
                    DocTree.Controller.viewChangedParent(CaseFileSplit.Model.DOC_TYPE_CASE_FILE, Acm.goodValue(parentCaseFile.id));
                    DocTree.View.expandTopNode();
                }
            }
            ,uploadForm: function(type, folderId, onCloseForm) {
                var caseFileId = CaseFileSplit.View.getActiveCaseFileId();
                var caseFile = CaseFileSplit.View.getActiveCaseFile();
                if (CaseFileSplit.Model.Detail.validateCaseFile(caseFile)) {
                    var url = null;
                    var fileType = CaseFileSplit.View.MicroData.findFileTypeByType(type);
                    if (fileType) {
                        url = Acm.goodValue(fileType.url);
                    }
                    if (Acm.isNotEmpty(url)) {
                        // an apostrophe in case title will make Frevvo throw up.  Need to encode it here, then rules in
                        // the Frevvo form will decode it.
                        var caseTitle = Acm.goodValue(caseFile.title);
                        caseTitle = caseTitle.replace("'", "_0027_"); // 0027 is the Unicode string for apostrophe

                        url = url.replace("_data=(", "_data=(type:'case', caseId:'" + caseFileId
                            + "',caseNumber:'" + Acm.goodValue(caseFile.caseNumber)
                            + "',caseTitle:'" + caseTitle
                            + "',casePriority:'" + Acm.goodValue(caseFile.priority)
                            + "',folderId:'" + folderId
                            + "',"
                        );
                        Acm.Dialog.openWindow(url, "", 1060, $(window).height() - 30, onCloseForm);
                    }
                }
            }
        }


    ,Participants: {
        create: function() {
            this.$divParticipants    = $("#divParticipants");
            this.createJTableParticipants(this.$divParticipants);

            Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_RETRIEVED_PARENT_CASE_FILE             ,this.onModelRetrievedParentCaseFile);
        }
        ,onInitialized: function() {
        }
        ,onModelRetrievedParentCaseFile: function(parentCaseFile){
            if (parentCaseFile.hasError) {
                Acm.View.MessageBoard("Error retrieving casefile", parentCaseFile.errorMsg);
            }
            else{
                AcmEx.Object.JTable.load(CaseFileSplit.View.Participants.$divParticipants);
            }
        }

        ,createJTableParticipants: function($s) {
            AcmEx.Object.JTable.useBasic($s, {
                title: $.t("casefile:participants.table.title")
                ,paging: true
                ,sorting: true
                ,pageSize: 10 //Set page size (default: 10)
                ,messages: {
                    addNewRecord: $.t("casefile:participants.msg.add-new-record")
                }
                ,actions: {
                    listAction: function(postData, jtParams) {
                        var rc = AcmEx.Object.JTable.getEmptyRecords();
                        var c = CaseFileSplit.Model.Detail.cacheParentCaseFile.get(CaseFileSplit.View.MicroData.parentCaseFileId);
                        if (CaseFileSplit.Model.Detail.validateCaseFile(c)) {
                            for (var i = 0; i < c.participants.length; i++) {
                                var participant = c.participants[i];
                                var record = {};
                                record.id = Acm.goodValue(participant.id, 0);
                                record.title = Acm.goodValue(participant.participantLdapId);
                                record.type = Acm.goodValue(participant.participantType);
                                rc.Records.push(record);
                            }
                            rc.TotalRecordCount = rc.Records.length;
                        }
                        return rc;
                    }
                }
                ,fields: {
                    id: {
                        title: $.t("casefile:participants.table.field.id")
                        ,key: true
                        ,list: false
                        ,create: false
                        ,edit: false
                    }
	                ,type: {
	                    title: $.t("casefile:participants.table.field.type")
	                    ,width: '30%'
	                    ,options: CaseFileSplit.Model.Lookup.getParticipantTypes()
	                    ,display: function (data) {
                            if (data.record.type == '*') {
                            	// Default user. This is needed to show default user in the table.
                        		// I am setting it here, because i don't want to show it in the popup while
                        		// creating new participant. If we set it in the popup, it should be removed from here.
                        		// This is used only to recognize the * type.
                            	return '*';
                            } else {
                            	var options = CaseFileSplit.Model.Lookup.getParticipantTypes();
                            	return options[data.record.type];
                            }
                        }
	                }
                    ,title: {
                        title: $.t("casefile:participants.table.field.name")
                        ,width: '70%'
                        ,dependsOn: 'type'
                        ,options: function (data) {
                        	if (data.dependedValues.type == '*') {
                        		// Default user. This is needed to show default user in the table.
                        		// I am setting it here, because i don't want to show it in the popup while
                        		// creating new participant. If we set it in the popup, it should be removed from here.
                        		// This is used only to recognize the * type.
                        		return {"*": "*"}
                        	}else if (data.dependedValues.type == 'owning group') {
                        		var caseFileId = CaseFileSplit.View.MicroData.parentCaseFileId;
                        		return Acm.createKeyValueObject(CaseFileSplit.Model.Lookup.getGroups(caseFileId));
                    		} else {
                    			return Acm.createKeyValueObject(CaseFileSplit.Model.Lookup.getUsers());
                    		}
                        }
                    }
                }
            });
        }
    }

    ,Notes: {
        create: function() {
            this.$divNotes          = $("#divNotes");
            this.createJTableNotes(this.$divNotes);

            Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_RETRIEVED_PARENT_CASE_FILE             ,this.onModelRetrievedParentCaseFile);
        }
        ,onInitialized: function() {
        }
        ,onModelRetrievedParentCaseFile: function(parentCaseFile){
            if (parentCaseFile.hasError) {
                Acm.View.MessageBoard(parentCaseFile.errorMsg);
            }
            else{
                AcmEx.Object.JTable.load(CaseFileSplit.View.Notes.$divNotes);
            }
        }


        ,_makeJtData: function(noteList) {
            var jtData = AcmEx.Object.JTable.getEmptyRecords();
            if (noteList) {
                for (var i = 0; i < noteList.length; i++) {
                    var Record = {};
                    Record.id         = Acm.goodValue(noteList[i].id, 0);
                    Record.note       = Acm.goodValue(noteList[i].note);
                    //Record.created    = Acm.getDateFromDatetime(noteList[i].created);
                    Record.created    = (Acm.getDateFromDatetime2(noteList[i].created,$.t("common:date.short")));
                    Record.creator    = Acm.__FixMe__getUserFullName(Acm.goodValue(noteList[i].creator));
                    jtData.Records.push(Record);
                }
                jtData.TotalRecordCount = noteList.length;
            }
            return jtData;
        }
        ,createJTableNotes: function($jt) {
            var sortMap = {};
            sortMap["created"] = "created";

            AcmEx.Object.JTable.usePaging($jt
                ,{
                    title: $.t("casefile:notes.table.title")
                    ,paging: true
                    ,sorting: true //fix me
                    ,pageSize: 10 //Set page size (default: 10)
                    ,selecting: true
                    ,multiselect: false
                    ,selectingCheckboxes: false
                    ,messages: {
                        addNewRecord: $.t("casefile:notes.msg.add-new-record")
                    }
                    ,actions: {
                        pagingListAction: function (postData, jtParams, sortMap) {
                            var caseFileId = CaseFileSplit.View.MicroData.parentCaseFileId;
                            if (0 >= caseFileId) {
                                return AcmEx.Object.JTable.getEmptyRecords();
                            }

                            var noteList = CaseFileSplit.Model.Notes.cacheNoteList.get(caseFileId);
                            if (noteList) {
                                return CaseFileSplit.View.Notes._makeJtData(noteList);

                            } else {
                                return CaseFileSplit.Service.Notes.retrieveNoteListDeferred(caseFileId
                                    ,postData
                                    ,jtParams
                                    ,sortMap
                                    ,function(data) {
                                        var noteList = data;
                                        return CaseFileSplit.View.Notes._makeJtData(noteList);
                                    }
                                    ,function(error) {
                                    }
                                );
                            }  //end else
                        }
                    }
                    ,fields: {
                        id: {
                            title: $.t("casefile:notes.table.field.id")
                            ,key: true
                            ,list: false
                            ,create: false
                            ,edit: false
                            ,defaultvalue : 0
                        }
                        ,note: {
                            title: $.t("casefile:notes.table.field.note")
                            ,type: 'textarea'
                            ,width: '50%'
                            ,edit: true
                        }
                        ,created: {
                            title: $.t("casefile:notes.table.field.created")
                            ,width: '15%'
                            ,edit: false
                            ,create: false
                        }
                        ,creator: {
                            title: $.t("casefile:notes.table.field.creator")
                            ,width: '15%'
                            ,edit: false
                            ,create: false
                        }
                    } //end field
                } //end arg
                ,sortMap
            );
        }
    }
    ,Summary:{
        create: function() {
            this.$btnSplitCase = $("#btnSplitCase").on("click", function(e){CaseFileSplit.View.Summary.onClickBtnSplitCase(e,this);});

            Acm.Dispatcher.addEventListener(CaseFileSplit.Controller.MODEL_SPLIT_CASE_FILE ,this.onModelSplitCaseFile);
        }
        ,onInitialized: function() {
        }
        ,onClickBtnSplitCase: function(event,ctrl) {
            var summary = this.getSummary();
            if(CaseFileSplit.Model.Summary.validateSummary(summary)){
                App.View.MessageBoard.show($.t("casefilesplit:split-submitted-message"));
                CaseFileSplit.Controller.viewRetrievedSummary(summary);
            }
        }
        ,onModelSplitCaseFile: function(splitCaseFile){
            if(splitCaseFile.hasError) {
                App.View.MessageBoard.show("Split failed" , splitCaseFile.errorMsg);
            }
            else{
                if(CaseFileSplit.Model.Detail.validateCaseFile(splitCaseFile)){
                    //var url = "/plugin/casefile/" + Acm.goodValue(splitCaseFile.id);
                    //App.View.gotoPage(url);
                    var splitInfo = new Acm.Model.LocalData("AcmSplitTmp");
                    splitInfo.set(Acm.goodValue(splitCaseFile.id));
                    window.close();
                }
            }
        }
        ,getRibbonInformation: function(){
            var ribbon = {};
            ribbon.originalCaseId = Acm.goodValue(CaseFileSplit.View.Detail.getOriginalCaseId());
            ribbon.caseTitle = Acm.goodValue(CaseFileSplit.View.Detail.getCaseTitle());
            ribbon.incidentDate = Acm.goodValue(CaseFileSplit.View.Detail.getIncidentDate());
            ribbon.assignee = Acm.goodValue(CaseFileSplit.View.Detail.getAssignee());
            ribbon.group = Acm.goodValue(CaseFileSplit.View.Detail.getGroup());
            ribbon.subjectType = Acm.goodValue(CaseFileSplit.View.Detail.getSubjectType());
            ribbon.priority = Acm.goodValue(CaseFileSplit.View.Detail.getPriority());
            ribbon.dueDate = Acm.goodValue(CaseFileSplit.View.Detail.getDueDate());
            return ribbon;
        }
        ,getAttachments: function() {
            var selNodes = DocTree.View.tree.getSelectedNodes();
            var attachments = [];
            if (DocTree.View.validateNodes(selNodes)) {
                for (var i = 0; i < selNodes.length; i++) {
                    if (DocTree.View.isFolderNode(selNodes[i])) {
                        attachments.push({"id": selNodes[i].data.objectId, "type": "folder"});
                    } else if (DocTree.View.isFileNode(selNodes[i])) {
                        attachments.push({"id": selNodes[i].data.objectId, "type": "document"});
                    }
                }
            }
            return attachments;
        }
        ,getSummary: function(){
            var summary = {};
            var ribbonInfo = this.getRibbonInformation();
            var attachments = this.getAttachments();
            summary.caseFileId =  Acm.goodValue(ribbonInfo.originalCaseId);
            summary.attachments = attachments;
            summary.preserveFolderStructure = true;
            return summary;
        }
    }

};

