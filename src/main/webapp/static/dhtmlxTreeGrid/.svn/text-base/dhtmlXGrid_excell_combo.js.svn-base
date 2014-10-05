/**
*	@desc: integration with dhtmlxCombo editor
*	@returns: dhtmlxGrid cell editor object
*	@type: public
*/
function eXcell_combo(cell){

		try{
			this.cell = cell;
			this.grid = this.cell.parentNode.grid;
		}catch(er){}
		/**
		*	@desc: method called by grid to start editing
		*/
	this.edit = function(){
		val = this.getValue();        
		this.cell.innerHTML="";
		dhx_globalImgPath="combo/img/";
		this.obj = new dhtmlXCombo(this.cell,"combo",this.cell.offsetWidth-2);
		this.obj.DOMelem.style.border = "0";
		this.obj.DOMelem.style.height = "18px";
		switch(this.cell.loadingMode){
			case "0":
				var selfc=this;
				this.obj.loadXML(this.cell._url,function(){
					//	if (!selfc.cell._cval)
							selfc.obj.setComboValue(val);
					/*	else
							selfc.obj.selectOption(selfc.obj.getOption(selfc.cell._сval));*/
					})
				break	
			case "1":
				this.obj.enableFilteringMode(true,this.cell._url,true,true)
				break
			case "2":
				for(var i = 0; i < options.length; i++){
					this.obj.addOption(i,options[i].firstChild.data)					
				}
				break	
		}
		if(this.cell.loadingMode == "0") this.obj.setComboText("")
	}
	
		/**
		*	@desc: get real value of the cell
		*/
		
	this.getValue = function(val){
		//if (this.cell._cval) return this.cell._cval;
		return this.cell.innerHTML.toString();
	}
		/**
		*	@desc: set formated value to the cell
		*/
	this.setValue = function(val){
	
		if (typeof(val)=="object")
		{
			if (!val.tagName){
				switch(val.type){
					case 1:
						this.cell.loadingMode="1";
						this.cell._url=val.url;
						break;
					default:
						this.cell.loadingMode="0";
						this.cell._url=val.url;
						break;
				}
				val=val.value||"";
			}
			else{
				this.cell.loadingMode = val.getAttribute("xmlcontent")
				if(this.cell.loadingMode == "2"){
					options=this.grid.xmlLoader.doXPath(".//option",val);
					val = options[0].firstChild.data
				}
				else{
					var childNumber = val.childNodes.length
				
					for(var i = 0; i < childNumber ; i++){
						if(val.childNodes[i].tagName=="url")
							this.cell._url=val.childNodes[i].childNodes[0].data
					}
				
					for(var i = 0; i < childNumber; i++){				
						if((typeof(val)=="object") && (val.childNodes[i].tagName=="value"))
							val = val.childNodes[i].childNodes[0].data
					}
				}
			}
		}
		
			this.setCValue(val);
			
		
	}
		/**
		*	@desc: this method called by grid to close editor
		*/
	this.detach = function(){
		if (!this.obj.getComboText() || this.obj.getComboText().toString()._dhx_trim()==""){
			this.setCValue("");
    	}
		else this.setCValue(this.obj.getComboText(),this.obj.getActualValue())
		this.cell._cval=this.obj.getActualValue();
		this.obj.closeAll();
		return true;
	}
}
eXcell_combo.prototype = new eXcell;
