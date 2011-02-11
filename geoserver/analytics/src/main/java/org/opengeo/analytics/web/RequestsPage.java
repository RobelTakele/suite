package org.opengeo.analytics.web;

import static org.geoserver.monitor.rest.RequestResource.toQueryString;

import java.util.Date;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.model.PropertyModel;
import org.geoserver.monitor.Query;
import org.geoserver.monitor.web.MonitorBasePage;

public class RequestsPage extends MonitorBasePage {

    Query query;
    RequestDataTablePanel requestTable;
    
    public RequestsPage(Query query) {
        this(new RequestDataProvider(query));
    }
    
    public RequestsPage(Query query, String title) {
        this(new RequestDataProvider(query), title);
    }
    
    public RequestsPage(RequestDataProvider provider) {
        this(provider, "Requests");
    }
    
    public RequestsPage(RequestDataProvider provider, String title) {
        this.query = provider.query;
        
        add(new Label("title", title));
        Form form = new Form("form");
        add(form);
        
        form.add(new TimeSpanPanel("timeSpan", new PropertyModel<Date>(this, "query.fromDate"), 
            new PropertyModel<Date>(this, "query.toDate")));
        form.add(new AjaxButton("refresh") {
            @Override
            protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
                target.addComponent(requestTable);
            }
        });
        
        requestTable = new RequestDataTablePanel("table", provider);
        requestTable.setOutputMarkupId(true);
        requestTable.setPageable(true);
        requestTable.setItemsPerPage(25);
        requestTable.setFilterable(false);
        add(requestTable);
        
        ExternalLink csvLink = new ExternalLink("csv", 
                "../rest/monitor/requests.csv" + toQueryString(provider.getQuery()));;
        add(csvLink);
        
        ExternalLink excelLink = new ExternalLink("excel", 
                "../rest/monitor/requests.xls" + toQueryString(provider.getQuery()));;
        add(excelLink);
      
    }
}
