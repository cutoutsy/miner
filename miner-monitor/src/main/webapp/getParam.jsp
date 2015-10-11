<%@ page import="java.util.Map" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.ArrayList" %>
<%
  Map<String,String> wmap = new HashMap<String,String>();
  Map<String,String> pmap = new HashMap<String,String>();
  Map<String,String> tmap = new HashMap<String,String>();
  Map<String,String> dmap = new HashMap<String,String>();
  Map<String,String> rmap = new HashMap<String,String>();
  Map<String,String> rmap1 = new HashMap<String,String>();
  Map<String,String> rmap2 = new HashMap<String,String>();
  Map<String,String> rmap3 = new HashMap<String,String>();
  Map<String,String> rmap4 = new HashMap<String,String>();
  ArrayList<Map<String,String>> list = new ArrayList<Map<String, String>>();
  wmap.put("wwid",new String(request.getParameter("wwid").getBytes("ISO-8859-1"),"UTF-8"));
  wmap.put("wname",new String(request.getParameter("wname").getBytes("ISO-8859-1"),"UTF-8"));
  wmap.put("wdescription",new String(request.getParameter("wdescription").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("pwid",new String(request.getParameter("pwid").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("ppid",new String(request.getParameter("ppid").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("pname",new String(request.getParameter("pname").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("pdescription",new String(request.getParameter("pdescription").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("pdatasource",new String(request.getParameter("pdatasource").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("pschedule",new String(request.getParameter("pschedule").getBytes("ISO-8859-1"),"UTF-8"));
  pmap.put("pprecondition",new String(request.getParameter("pprecondition").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("twid",new String(request.getParameter("twid").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("tpid",new String(request.getParameter("tpid").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("ttid",new String(request.getParameter("ttid").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("tdescription",new String(request.getParameter("tdescription").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("turlpattern",new String(request.getParameter("turlpattern").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("turlgenerate",new String(request.getParameter("turlgenerate").getBytes("ISO-8859-1"),"UTF-8"));
  tmap.put("tisloop",new String(request.getParameter("tisloop").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dwid",new String(request.getParameter("dwid").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dpid",new String(request.getParameter("dpid").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dtid",new String(request.getParameter("dtid").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("ddataid",new String(request.getParameter("ddataid").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("ddescription",new String(request.getParameter("ddescription").getBytes("ISO-8859-1"),"UTF-8"));

  dmap.put("dname1",new String(request.getParameter("dname1").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname2",new String(request.getParameter("dname2").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname3",new String(request.getParameter("dname3").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname4",new String(request.getParameter("dname4").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname5",new String(request.getParameter("dname5").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname6",new String(request.getParameter("dname6").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname7",new String(request.getParameter("dname7").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname8",new String(request.getParameter("dname8").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname9",new String(request.getParameter("dname9").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dname10",new String(request.getParameter("dname10").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("drowkey",new String(request.getParameter("drowkey").getBytes("ISO-8859-1"),"UTF-8"));

  dmap.put("dforeignkey",new String(request.getParameter("dforeignkey").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dforeignvalue",new String(request.getParameter("dforeignvalue").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dlink",new String(request.getParameter("dlink").getBytes("ISO-8859-1"),"UTF-8"));
  dmap.put("dprocessway",new String(request.getParameter("dprocessway").getBytes("ISO-8859-1"),"UTF-8"));

  rmap.put("rwid5",new String(request.getParameter("rwid5").getBytes("ISO-8859-1"),"UTF-8"));
  rmap.put("rpid5",new String(request.getParameter("rpid5").getBytes("ISO-8859-1"),"UTF-8"));
  rmap.put("rtid5",new String(request.getParameter("rtid5").getBytes("ISO-8859-1"),"UTF-8"));
  rmap.put("rtagname5",new String(request.getParameter("rtagname5").getBytes("ISO-8859-1"),"UTF-8"));
  rmap.put("rpath5",new String(request.getParameter("rpath5").getBytes("ISO-8859-1"),"UTF-8"));
  rmap1.put("rwid1",new String(request.getParameter("rwid1").getBytes("ISO-8859-1"),"UTF-8"));
  rmap1.put("rpid1",new String(request.getParameter("rpid1").getBytes("ISO-8859-1"),"UTF-8"));
  rmap1.put("rtid1",new String(request.getParameter("rtid1").getBytes("ISO-8859-1"),"UTF-8"));
  rmap1.put("rtagname1",new String(request.getParameter("rtagname1").getBytes("ISO-8859-1"),"UTF-8"));
  rmap1.put("rpath1",new String(request.getParameter("rpath1").getBytes("ISO-8859-1"),"UTF-8"));

  rmap2.put("rwid2",new String(request.getParameter("rwid2").getBytes("ISO-8859-1"),"UTF-8"));
  rmap2.put("rpid2",new String(request.getParameter("rpid2").getBytes("ISO-8859-1"),"UTF-8"));
  rmap2.put("rtid2",new String(request.getParameter("rtid2").getBytes("ISO-8859-1"),"UTF-8"));
  rmap2.put("rtagname2",new String(request.getParameter("rtagname2").getBytes("ISO-8859-1"),"UTF-8"));
  rmap2.put("rpath2",new String(request.getParameter("rpath2").getBytes("ISO-8859-1"),"UTF-8"));
  rmap3.put("rwid3",new String(request.getParameter("rwid3").getBytes("ISO-8859-1"),"UTF-8"));
  rmap3.put("rpid3",new String(request.getParameter("rpid3").getBytes("ISO-8859-1"),"UTF-8"));
  rmap3.put("rtid3",new String(request.getParameter("rtid3").getBytes("ISO-8859-1"),"UTF-8"));
  rmap3.put("rtagname3",new String(request.getParameter("rtagname3").getBytes("ISO-8859-1"),"UTF-8"));
  rmap3.put("rpath3",new String(request.getParameter("rpath3").getBytes("ISO-8859-1"),"UTF-8"));
  rmap4.put("rwid4",new String(request.getParameter("rwid4").getBytes("ISO-8859-1"),"UTF-8"));
  rmap4.put("rpid4",new String(request.getParameter("rpid4").getBytes("ISO-8859-1"),"UTF-8"));
  rmap4.put("rtid4",new String(request.getParameter("rtid4").getBytes("ISO-8859-1"),"UTF-8"));
  rmap4.put("rtagname4",new String(request.getParameter("rtagname4").getBytes("ISO-8859-1"),"UTF-8"));
  rmap4.put("rpath4",new String(request.getParameter("rpath4").getBytes("ISO-8859-1"),"UTF-8"));
  list.add(rmap);
  list.add(rmap1);
  list.add(rmap2);
  list.add(rmap3);
  list.add(rmap4);
%>
