
//"mygrid.toExcel('../server/generate.php');

dhtmlXGridObject.prototype.toPDF=function(url,mode,header,footer,rows){

	mode = mode || "color";	
	var grid = this;
	grid._asCDATA = true;
		
	eXcell_ch.prototype.getContent = function(){
		return this.getValue();
	};
	eXcell_ra.prototype.getContent = function(){
		return this.getValue();
	};
	
	
	function xml_footer() {
		var spans = [];
		for (var i=1; i<grid.ftr.rows.length; i++){
			spans[i]=[];
			for (var j=0; j<grid._cCount; j++){
				var cell = grid.ftr.rows[i].childNodes[j];
				if (!spans[i][j])
					spans[i][j]=[0,0];
				if (cell)
					spans[i][cell._cellIndexS]=[cell.colSpan, cell.rowSpan]
			}
		}

	    xml="<foot>"+grid._serialiseExportConfig(spans,false).replace(/^<head/,"<columns").replace(/head>$/,"columns>");

	    for (var i=2; i < grid.ftr.rows.length; i++) {
	    	xml+="\n<columns>";
	    	for (var j=0; j < grid._cCount; j++) {
	    		var s = spans[i][j];
	    		var rpans = (( s[1] && s[1] > 1 ) ? ' rowspan="'+s[1]+'" ' : "")+(( s[0] && s[0] > 1 ) ? ' colspan="'+s[0]+'" ' : "");		
	    		var colLable = grid.getFooterLabel(j,i-1);
	    		
	    		xml+="<column "+rpans+"><![CDATA["+ colLable +"]]></column>";
	    	};
	    	xml+="</columns>";
	    };
	    xml+="</foot>\n";

	    return xml;
	};
	function xml_top(profile) {
		var spans = [];
		for (var i=1; i<grid.hdr.rows.length; i++){
			spans[i]=[];
			for (var j=0; j<grid._cCount; j++){
				var cell = grid.hdr.rows[i].childNodes[j];
				if (!spans[i][j])
					spans[i][j]=[0,0];
				if (cell)
					spans[i][cell._cellIndexS]=[cell.colSpan, cell.rowSpan]
			}
		}
		
	    var xml = "<rows profile='"+profile+"'";
	       if (header)
	          xml+=" header='"+header+"'";
	       if (footer)
	          xml+=" footer='"+footer+"'";
	    xml+="><head>"+grid._serialiseExportConfig(spans,true).replace(/^<head/,"<columns").replace(/head>$/,"columns>");

	      
	    for (var i=2; i < grid.hdr.rows.length; i++) {
	    	xml+="\n<columns>";
	    	for (var j=0; j < grid._cCount; j++) {
	    		var s = spans[i][j];
	    		var rpans = (( s[1] && s[1] > 1 ) ? ' rowspan="'+s[1]+'" ' : "")+(( s[0] && s[0] > 1 ) ? ' colspan="'+s[0]+'" ' : "");		
	    		var colLable = grid.getColumnLabel(j,i-1);
	    		
	    		xml+="<column "+rpans+"><![CDATA["+ colLable +"]]></column>";
	    	};
	    	xml+="</columns>";
	    };
	    xml+="</head>\n";
	    
	  
	    
	    return xml;
	};
	
	function xml_body() {
	
		var xml =[];
	    if (rows){
	    	for (var i=0; i<rows.length; i++){
	    			xml.push(xml_row(grid.getRowIndex(rows[i])));
	    	}
	    }else{
	    	for (var i=0; i<grid.getRowsNum(); i++){
	    		xml.push(xml_row(i));
	    	}
	    }

	    		
	    return xml.join("\n");
	}	    		
	function xml_row(ind){
	
//		if (!grid.rowsBuffer[ind]) return "";
//		var r = grid.render_row(ind);
		var row_id = grid.getRowId(ind);
		var r = grid.getRowById(row_id);
	     var col_length = grid._cCount;
//		if (r.style.display=="none") return "";

		var xml = "<row>";
		
//		for (var i=0; i < grid._cCount; i++) {
//			if ((!this._srClmn)||(this._srClmn[i])){
//				var cell = grid.cells(r.idd, i);
////				var value = cell.cell.innerHTML;	
//				var value = cell.getValue();
//				xml+="<cell><![CDATA["+ value +"]]></cell>";
////				xml+="<cell><![CDATA["+(cell.getContent?cell.getContent():cell.getTitle())+"]]></cell>";
//			}
//		};
		
		
		for (var i=0; i < col_length; i++) {
			
				var c = grid.cells(r.idd, i);
				if (c.getImage) var value=c.cell.innerHTML;
           	   else var value = c.getValue();
//           	   value = value===""?"&nbsp;":value;
           	   xml+="<cell><![CDATA["+ value +"]]></cell>";
           	   
		
           }		
		

		
		return xml+"</row>";
	}
	function xml_end(){
	    var xml = "</rows>";
	    return xml;
	}


//   alert(xml_footer().replace("\u2013", "-"));

    var grid_xml_string =  encodeURI(xml_top(mode).replace("\u2013", "-") + xml_body() + xml_footer().replace("\u2013", "-")+  xml_end());

	var d=document.createElement("div");
	d.style.display="none";
	document.body.appendChild(d);
	var uid = "form_"+grid.uid();
	d.innerHTML = '<form id="'+uid+'" method="post" target="_blank" action="'+url+'" accept-charset="UTF-8"><input type="hidden" name="grid_xml" id="grid_xml"/> </form>';
	document.getElementById(uid).firstChild.value = grid_xml_string;
	document.getElementById(uid).submit();
	d.parentNode.removeChild(d);
	grid = null;	
	
	
	
	
	
	
	
	
};
dhtmlXGridObject.prototype._serialiseExportConfig=function(spans,isHead){
	var out = "<head>";
    var col_length = this._cCount;
	for (var i = 0; i < this.hdr.rows[0].cells.length; i++){
		if (this._srClmn && !this._srClmn[i]) continue;
		var sort = this.fldSort[i];
		if (sort == "cus"){
			sort = this._customSorts[i].toString();
			sort=sort.replace(/function[\ ]*/,"").replace(/\([^\f]*/,"");
		}
		var s = spans[1][i];
	
		var rpans = (( s[1] && s[1] > 1 ) ? ' rowspan="'+s[1]+'" ' : "")+(( s[0] && s[0] > 1 ) ? ' colspan="'+s[0]+'" ' : "");		
		out+="<column "+rpans+" width='"+this.getColWidth(i)+"' align='"+this.cellAlign[i]+"' type='"+this.cellType[i]
			+"' sort='"+(sort||"na")+"' color='"+(this.columnColor[i]||"")+"'"
			+(this.columnIds[i]
				? (" id='"+this.columnIds[i]+"'")
				: "")+">";
		if (this._asCDATA){
			if(isHead)
				out+="<![CDATA["+   this.getColumnLabel(i) +"]]>";
			else
				out+="<![CDATA["+   this.getFooterLabel(i) +"]]>";
		}else{
			if(isHead)
				out+=this.getColumnLabel(i);
			else
				out+=this.getFooterLabel(i);
		}
			
		
			
		var z = this.getCombo(i);

		if (z)
			for (var j = 0; j < z.keys.length; j++)out+="<option value='"+z.keys[j]+"'>"+z.values[j]+"</option>";
		out+="</column>"
	}
	return out+="</head>";
};

dhtmlXGridObject.prototype.toExcel = dhtmlXGridObject.prototype.toPDF;