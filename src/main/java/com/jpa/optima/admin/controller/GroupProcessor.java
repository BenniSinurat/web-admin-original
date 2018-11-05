package com.jpa.optima.admin.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.ws.Holder;

import org.bellatrix.services.ws.groups.CreateGroupsRequest;
import org.bellatrix.services.ws.groups.Exception_Exception;
import org.bellatrix.services.ws.groups.Group;
import org.bellatrix.services.ws.groups.GroupService;
import org.bellatrix.services.ws.groups.Groups;
import org.bellatrix.services.ws.groups.LoadGroupsByIDRequest;
import org.bellatrix.services.ws.groups.LoadGroupsByIDResponse;
import org.bellatrix.services.ws.groups.LoadGroupsRequest;
import org.bellatrix.services.ws.groups.LoadGroupsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GroupProcessor {
	@Autowired
	private ContextLoader contextLoader;
	
	public String loadAllGroup(Integer currentPage, Integer pageSize) throws MalformedURLException{
		URL url = new URL(contextLoader.getHostWSUrl()+"groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupService");
		GroupService service = new GroupService(url, qName);
		Group client = service.getGroupPort();

		org.bellatrix.services.ws.groups.Header headerGroup = new org.bellatrix.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.groups.Header> groupHeaderAuth = new Holder<org.bellatrix.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;
		System.out.println("Page : "+ pageSize);
		LoadGroupsRequest loadGroupsReq = new LoadGroupsRequest();
		loadGroupsReq.setCurrentPage(currentPage);
		loadGroupsReq.setPageSize(pageSize);
		
		LoadGroupsResponse loadGroupsRes = client.loadAllGroups(groupHeaderAuth, loadGroupsReq);
		System.out.println("Response: "+loadGroupsRes.toString());
		Map<String, Object> trxMap = new HashMap<String, Object>();
		trxMap.put("data", loadGroupsRes.getGroupsList());
		trxMap.put("recordsTotal", loadGroupsRes.getTotalRecords());
		trxMap.put("recordsFiltered", loadGroupsRes.getTotalRecords());
		System.out.println("Data: "+trxMap.get("name"));
		return Utils.toJSON(trxMap);
	}

	public String getGroupNameByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupService");
		GroupService service = new GroupService(url, qName);
		Group client = service.getGroupPort();

		org.bellatrix.services.ws.groups.Header headerGroup = new org.bellatrix.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.groups.Header> groupHeaderAuth = new Holder<org.bellatrix.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;

		LoadGroupsByIDRequest loadGroupsByIDRequest = new LoadGroupsByIDRequest();
		loadGroupsByIDRequest.setId(id);

		LoadGroupsByIDResponse loadGroupsByIDResponse = client.loadGroupsByID(groupHeaderAuth, loadGroupsByIDRequest);
		String groupName = loadGroupsByIDResponse.getGroups().getName();

		return groupName;
	}
	
	public List<String> getListGroup() throws MalformedURLException{
		URL url = new URL(contextLoader.getHostWSUrl()+"groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupService");
		GroupService service = new GroupService(url, qName);
		Group client = service.getGroupPort();

		org.bellatrix.services.ws.groups.Header headerGroup = new org.bellatrix.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.groups.Header> groupHeaderAuth = new Holder<org.bellatrix.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;
		
		LoadGroupsRequest loadGroupsRequest = new LoadGroupsRequest();
		loadGroupsRequest.setCurrentPage(0);
		loadGroupsRequest.setPageSize(100);
		LoadGroupsResponse loadGroupsResponse = client.loadAllGroups(groupHeaderAuth, loadGroupsRequest);
		
		List<String> groupList = new LinkedList<String>();
		if(loadGroupsResponse.getGroupsList().size() > 0){
			int i;
			for(i = 0; i < loadGroupsResponse.getGroupsList().size(); i++){
				groupList.add(loadGroupsResponse.getGroupsList().get(i).getId()+"-"+loadGroupsResponse.getGroupsList().get(i).getName());
			}
		}
		
		return groupList;
	}
	
	public Map<String, Object> loadGroupsByID(Integer id) throws MalformedURLException {
		URL url = new URL(contextLoader.getHostWSUrl()+"groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupService");
		GroupService service = new GroupService(url, qName);
		Group client = service.getGroupPort();
		
		org.bellatrix.services.ws.groups.Header headerGroup = new org.bellatrix.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.groups.Header> groupHeaderAuth = new Holder<org.bellatrix.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;
		
		LoadGroupsByIDRequest loadGroupsByIDReq = new LoadGroupsByIDRequest();
		loadGroupsByIDReq.setId(id);
		LoadGroupsByIDResponse loadGroupsByIDRes = client.loadGroupsByID(groupHeaderAuth, loadGroupsByIDReq);
		
		Map<String, Object> groupDetailsContentMap = new HashMap<String, Object>();
		groupDetailsContentMap.put("id", loadGroupsByIDRes.getGroups().getId());
		groupDetailsContentMap.put("createdDate", loadGroupsByIDRes.getGroups().getFormattedCreatedDate());
		groupDetailsContentMap.put("name", loadGroupsByIDRes.getGroups().getName());
		groupDetailsContentMap.put("description", loadGroupsByIDRes.getGroups().getDescription());
		groupDetailsContentMap.put("notificationID", loadGroupsByIDRes.getGroups().getNotificationID());
		groupDetailsContentMap.put("maxPinRetry", loadGroupsByIDRes.getGroups().getMaxPinTries());
		groupDetailsContentMap.put("pinLength", loadGroupsByIDRes.getGroups().getPinLength());
		
		return groupDetailsContentMap;
	}
	
	public void createNewGroup(com.jpa.optima.admin.model.Group group) throws MalformedURLException, Exception_Exception{
		URL url = new URL(contextLoader.getHostWSUrl()+"groups?wsdl");
		QName qName = new QName(contextLoader.getHostWSPort(), "GroupService");
		GroupService service = new GroupService(url, qName);
		Group client = service.getGroupPort();
		
		org.bellatrix.services.ws.groups.Header headerGroup = new org.bellatrix.services.ws.groups.Header();
		headerGroup.setToken(contextLoader.getHeaderToken());
		Holder<org.bellatrix.services.ws.groups.Header> groupHeaderAuth = new Holder<org.bellatrix.services.ws.groups.Header>();
		groupHeaderAuth.value = headerGroup;
		
		Groups groups = new Groups();
		groups.setName(group.getName());
		groups.setDescription(group.getDescription());
		groups.setPinLength(group.getPinLength());
		
		CreateGroupsRequest createGroupsReq = new CreateGroupsRequest();
		createGroupsReq.setGroups(groups);
		
		client.createGroups(groupHeaderAuth, createGroupsReq);
	}
	
}
