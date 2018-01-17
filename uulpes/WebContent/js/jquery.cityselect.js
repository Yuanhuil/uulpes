/*
Ajax 三级省市联动
日期：2012-7-18

settings 参数说明
-----
url:省市数据josn文件路径
prov:默认省份
city:默认城市
dist:默认地区（县）
nodata:无数据状态
required:必选项
------------------------------ */
(function($){
	$.fn.citySelect=function(settings){
		if(this.length<1){return;};

		// 默认值
		settings=$.extend({
			url:"/pes/js/city.min.js",
			prov:null,
			city:null,
			dist:null,
			street:null,
			defaultprov:null,
			defaultcity:null,
			defaultdist:null,
			defaultstreet:null,
			nodata:null,
			required:false,
			showstreet:true
		},settings);

		var box_obj=this;
		var prov_obj=box_obj.find(".prov");
		var city_obj=box_obj.find(".city");
		var dist_obj=box_obj.find(".dist");
		var street_obj = box_obj.find(".street");
		var prov_val=settings.prov;
		var city_val=settings.city;
		var dist_val=settings.dist;
		var street_val = settings.street;
		var default_prov = settings.defaultprov;
		var default_city= settings.defaultcity;
		var default_dist= settings.defaultdist;
		var default_street= settings.defaultstreet;
		var select_prehtml=(settings.required) ? "" : "<option value=''>请选择</option>";
		var city_json;
		var showstreet = settings.showstreet;

		// 赋值市级函数
		var cityStart=function(){
			var prov_id=prov_obj.get(0).selectedIndex;
			var prov_bm = prov_obj.val();
			if(!settings.required){
				prov_id--;
			};
			city_obj.empty().attr("disabled",true);
			dist_obj.empty().attr("disabled",true);
			street_obj.empty().attr("disabled",true);
			
			// 遍历赋值市级下拉列表
			temp_html=select_prehtml;
			var citylist;
			$.each(city_json.citylist, function(i,prov){
				if(prov.code === prov_bm){
					citylist = prov.c;
					return false;
				}
			});
			if(prov_id<0||typeof(citylist)=="undefined"){
				if(settings.nodata=="none"){
					city_obj.css("display","none");
					dist_obj.css("display","none");
					street_obj.css("display","none");
				}else if(settings.nodata=="hidden"){
					city_obj.css("visibility","hidden");
					dist_obj.css("visibility","hidden");
					street_obj.css("visibility","hidden");
				};
				return;
			};
			if(default_city == null || default_city === ''){
				$.each(citylist,function(i,city){
					temp_html+="<option value='"+city.code+"'>"+city.n+"</option>";
				});
			}else{
				$.each(citylist,function(i,city){
					if(city.code === default_city){
						temp_html+="<option value='"+city.code+"'>"+city.n+"</option>";
					}
				});
			}
			city_obj.html(temp_html).attr("disabled",false).css({"display":"","visibility":""});
			distStart();
		};

		// 赋值地区（县）函数
		var distStart=function(){
			var prov_id=prov_obj.get(0).selectedIndex;
			var city_id=city_obj.get(0).selectedIndex;
			var prov_bm = prov_obj.val();
			var city_bm = city_obj.val();
			if(!settings.required){
				prov_id--;
				city_id--;
			};
			dist_obj.empty().attr("disabled",true);
			street_obj.empty().attr("disabled",true);
			
			
			// 遍历赋值市级下拉列表
			temp_html=select_prehtml;
			var distlist;
			$.each(city_json.citylist, function(i,prov){
				if(prov.code === prov_bm){
					$.each(prov.c, function(i,city){
						if(city.code === city_bm){
							distlist = city.a;
							return false;
						}
					});
					
				}
			});
			if(prov_id<0||city_id<0||typeof(distlist)=="undefined"){
				if(settings.nodata=="none"){
					dist_obj.css("display","none");
					street_obj.css("display","none");
				}else if(settings.nodata=="hidden"){
					dist_obj.css("visibility","hidden");
					street_obj.css("visibility","hidden");
				};
				return;
			};
			if(default_dist ==null || default_dist===''){
				$.each(distlist,function(i,dist){
					temp_html+="<option value='"+dist.code+"'>"+dist.s+"</option>";
				});
			}else{
				$.each(distlist,function(i,dist){
					if(dist.code === default_dist){
						temp_html+="<option value='"+dist.code+"'>"+dist.s+"</option>";
					}
				});
			}
			
			dist_obj.html(temp_html).attr("disabled",false).css({"display":"","visibility":""});
			streetStart();
		};
		
		var streetStart = function(){
			if(showstreet == false) return
			var prov_id=prov_obj.get(0).selectedIndex;
			var city_id=city_obj.get(0).selectedIndex;
			var dist_id=dist_obj.get(0).selectedIndex;
			if(!settings.required){
				prov_id--;
				city_id--;
				dist_id--;
			};
			var dist_selectvalue = dist_obj.val();
			if(dist_selectvalue == null || dist_selectvalue === "")
				return;
			temp_html=select_prehtml;
			$.ajax({
				url:"/pes/util/districtcontroller/getTowns.do",
				data:{"distid":dist_selectvalue},
				dataType:"json",
				type:"post",
				async:false,
				success:function(data){
					$.each(data,function(i,street){
						temp_html+="<option value='"+street.code+"'>"+street.name+"</option>";
					});
					street_obj.html(temp_html).attr("disabled",false).css({"display":"","visibility":""});
				},
				error:function(){
					alert("异常错误请联系管理员");
				}
			});
			
			
		};
		

		var init=function(){
			// 遍历赋值省份下拉列表
			temp_html=select_prehtml;
			if(default_prov == null || default_prov === ''){
				$.each(city_json.citylist,function(i,prov){
					temp_html+="<option value='"+prov.code+"'>"+prov.p+"</option>";
				});
			}else{
				$.each(city_json.citylist,function(i,prov){
					if(prov.code === default_prov){
						temp_html+="<option value='"+prov.code+"'>"+prov.p+"</option>";
					}
				});
			}
			
			prov_obj.html(temp_html);

			// 若有传入省份与市级的值，则选中。（setTimeout为兼容IE6而设置）
			setTimeout(function(){
				if(settings.prov!=null){
					prov_obj.val(settings.prov);
					cityStart();
					setTimeout(function(){
						if(settings.city!=null){
							city_obj.val(settings.city);
							distStart();
							setTimeout(function(){
								if(settings.dist!=null){
									dist_obj.val(settings.dist);
									streetStart();
									setTimeout(function(){
										if(settings.street!=null){
											street_obj.val(settings.street);
										}
									},1);
								};
							},1);
						};
					},1);
				};
			},1);

			// 选择省份时发生事件
			prov_obj.bind("change",function(){
				cityStart();
			});

			// 选择市级时发生事件
			city_obj.bind("change",function(){
				distStart();
			});
			dist_obj.bind("change",function(){
				streetStart();
			});
		};

		// 设置省市json数据
		if(typeof(settings.url)=="string"){
			$.getJSON(settings.url,function(json){
				city_json=json;
				init();
			});
		}else{
			city_json=settings.url;
			init();
		};
	};
})(jQuery);