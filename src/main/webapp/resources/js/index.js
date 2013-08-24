$(function() {
	
	$("#pageCount").data("category", "");
	//加载目录
	getAjaxRequest('rs/category', onCategoryDataReceived);
	$("#menu").menu({
		  select: function( event, ui ) {
			  //get category id
			  var category_id = ui.item.children().attr('id');
			  
			  //得到分类数据，并设置分类ID
			  getData(category_id, 1);
			  $("#pageCount").data("category", category_id);
		  }
	});
	
	//得到首页新闻数据
	var currentPage = 1; //当前页码 
	var totalCount,limitSize,totalPage; //总记录数，每页显示数，总页数 
	getData($("#pageCount").data("category"), 1);
	
	//点击下一页时load数据
	$(document).on("click","#pageCount span a", function(e){
		e.preventDefault();
		var rel = $(this).attr("rel");
		if(rel){
			getData($("#pageCount").data("category"), rel);
		}
	});

	function getData(category_id, page){  
	    $.ajax({ 
	        type: 'POST', 
	        url: 'rs/news?categoryId=' + category_id, 
	        data: {'currentPage':page, 'limitSize': 10, 'sortField': 'date', 'sortOrder': 'desc'},
	        dataType:'json', 
	        success:function(data){ 
	        	$("#newsContent").empty();
	            totalCount = data.totalCount; //总记录数 
	            limitSize = data.limitSize; //每页显示条数 
	            currentPage = data.currentPage; //当前页 
	            totalPage = data.totalPage; //总页数 
	            $('#newsContentTemplate').tmpl(data.rows).prependTo('#newsContent');
	        }, 
	        complete:function(){ //生成分页条 
	            getPageBar(); 
	        }, 
	        error:function(){ 
	            alert("数据加载失败"); 
	        } 
	    }); 
	}

	//获取分页条 
	function getPageBar(){ 
	    //页码大于最大页数 
	    if(currentPage > totalPage) currentPage = totalPage; 
	    //页码小于1 
	    if(currentPage < 1) currentPage = 1; 
	    pageStr = "<span>共" + totalCount + "条</span><span>" + currentPage +"/" + totalPage + "</span>"; 
	     
	    //如果是第一页 
	    if(currentPage == 1){ 
	        pageStr += "<span>首页</span><span>上一页</span>"; 
	    }else{ 
	        pageStr += "<span><a href='javascript:void(0)' rel='1'>首页</a></span><span><a href='javascript:void(0)' rel='"+(currentPage-1)+"'>上一页</a></span>"; 
	    } 
	     
	    //如果是最后页 
	    if(currentPage >= totalPage){ 
	        pageStr += "<span>下一页</span><span>尾页</span>"; 
	    }else{ 
	        pageStr += "<span><a href='javascript:void(0)' rel='"+(parseInt(currentPage)+1)+"'>下一页</a></span><span><a href='javascript:void(0)' rel='"+totalPage+"'>尾页</a></span>"; 
	    } 
	         
	    $("#pageCount").html(pageStr); 
	}
	
});

function getAjaxRequest(requestUrl, onSucessFunction) {
	$.ajax({
		url: requestUrl,
		type: 'GET',
		dataType: 'json',
		cache: false,
		async: false,
		success: onSucessFunction
	});
}

function onCategoryDataReceived(data) {
	for(var i = 0; i < data.length; i++){
		var child = "<li><a id='" + data[i].id + "' href='#'>" + data[i].name + "</a></li>";
		$("#menu").append(child);
	}
}

function formatDate(datetime) {
	var date = new Date(Number(datetime));
	return date.toString("yyyy-MM-dd HH:mm:ss");
}


