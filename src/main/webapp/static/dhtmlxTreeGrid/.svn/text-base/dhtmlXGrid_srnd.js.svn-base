/*
Copyright Scand LLC http://www.scbr.com
This version of Software is free for using in GNU GPL applications. For other use or to get Professional Edition please contact info@scbr.com to obtain license
*/ 
 
 dhtmlXGridObject.prototype.enableSmartRendering = function(mode,totalRows,bufferSize){
 if(!convertStringToBoolean(mode))return;
 this._dload = true;
 if(!this._srdh)this._srdh=20;

 if(!this.deleteRow_WSRD){
 this.deleteRow_WSRD=this.deleteRow;
 this.deleteRow=this.deleteRow_WSRDA;

 this._insertRowAt_WSRD=this._insertRowAt;
 this._insertRowAt=this._insertRowAt_WSRDA;
}


 this._dInc=12;
 this._dl_start=new Array();
 this._limitC=this.limit=totalRows;

 this.multiLine=false;
 this._dloadSize=Math.floor(parseInt(this.entBox.offsetHeight)/this._srdh)+2;
 this.obj.className+=" row20px";
 
 
// alert('bufferSize7777777777' +bufferSize)
// alert('this._dloadSize 7777777777' +this._dloadSize)
 
 
 this._dpref=bufferSize||this._dloadSize;
 

 
 
 if(this.hdr.childNodes[1])
 this._initD();
 else
 this._initDrF=true;

}
 

 dhtmlXGridObject.prototype._initD = function(){
 if((this.fldSort)&&(this.fldSort.length))
 for(var i=0;i<this.fldSort.length;i++)this.fldSort[i]="na";

 if(this.limit)
 this._fastAddRowSpacer(0,this.limit*this._srdh);


 this._initDrF=false;
}


 
 dhtmlXGridObject.prototype.enableDOMLimit = function(mode,limit){
 if(!convertStringToBoolean(mode))return;
 this._dom_limit=limit||1000;
}

 
 

 
 dhtmlXGridObject.prototype._checkPref=function(start,direction){
 if(_isKHTML)return start;
 var i=1;
 for(i;i<=this._dpref;i++)
 if(((i*direction+start)<0)||((i*direction+start)>this.limit)||(this.rowsCol[i*direction+start])||(this.rowsBuffer[i*direction+start]))break;
 return start+(i-1)*direction;
}
 
 

 
 
 
 dhtmlXGridObject.prototype._simplifyDom=function(a,b,c){
 var count=0;

 for(var i=0;i<this.obj._rowslength();i++)
 if((i<a)||(i>b)){
 var z=this.obj._rows(i);
 if((!z._rLoad)&&(!z._sRow)){

 if((z.previousSibling)&&((z.previousSibling._sRow)||(z.previousSibling._rLoad)))
{
 
 var ind=this.rowsCol._dhx_find(z);
 var zprev=z.previousSibling;


 this.rowsBuffer[1][ind]=z;

 zprev._sRow=true;
 z.parentNode.removeChild(z);
 this.rowsAr[this.rowsCol[ind].idd]=null;

 this.rowsCol[ind].idd=id;
 this.rowsCol[ind]=null;

 this._fixHeight(zprev,-this._srdh);
 i--;
}else
 if((z.nextSibling)&&((z.nextSibling._sRow)||(z.nextSibling._rLoad)))
{
 
 var ind=this.rowsCol._dhx_find(z);
 var zprev=z.nextSibling;


 this.rowsBuffer[1][ind]=z;

 zprev._sRow=true;
 z.parentNode.removeChild(z);
 this.rowsAr[this.rowsCol[ind].idd]=null;

 this.rowsCol[ind].idd=id;
 this.rowsCol[ind]=null;

 this._fixHeight(zprev,-this._srdh);
 i--;
}
 else{
 var ind=this.rowsCol._dhx_find(z);
 this.rowsBuffer[1][ind]=z;
 

 var id='temp_dLoad_'+this._dInc;
 this._dInc++;
 var zn=this._fastAddRow(id,ind,true,ind);

 z.parentNode.removeChild(z);
 zn._sRow=true;
 this._fixHeight(zn,0);

}
 if(this.obj._rowslength()<=this._dom_limit)return;
}
}
}
 
 
 
 dhtmlXGridObject.prototype._addFromBufferSR=function(j){
 if((!this.rowsCol[j])||(this.rowsCol[j]._sRow))
 this._splitRowAt(j);
 else
 if((this.rowsBuffer[1][j])&&(this.rowsBuffer[1][j].tagName=="TR")){
 this.rowsCol[j].parentNode.insertBefore(this.rowsBuffer[1][j],this.rowsCol[j]);
 this.rowsCol[j].parentNode.removeChild(this.rowsCol[j]);
 this.rowsCol[j].grid=null;
 this.rowsCol[j]=this.rowsBuffer[1][j];
}


 if(this.rowsBuffer[1][j].tagName=="row"){
 if(this._cssEven){
 if(j%2==1)this.rowsCol[j].className=this._cssUnEven;
 else this.rowsCol[j].className=this._cssEven;
}
 this._fillRowFromXML(this.rowsCol[j],this.rowsBuffer[1][j],-1);

 this.changeRowId(this.rowsCol[j].idd,this.rowsBuffer[1][j].getAttribute("id"));
}
 else{
 this.rowsAr[this.rowsBuffer[1][j].idd]=this.rowsBuffer[1][j];
 this.rowsBuffer[1][j]._sRow=this.rowsBuffer[1][j]._rLoad=false;
}



 this.rowsCol[j]._rLoad=false;
 this.rowsBuffer[1][j]=null;
}

 
 dhtmlXGridObject.prototype._askRealRows=function(pos,afterCall){
 	
  afterCall = this.xmlLoader.waitCall;
  
//  this.posStart2 = pos == null?0:pos;  
  
//  this.posStart = pos;
//  alert(this.posStart2)
  
  
  
	//alert('this.limit 7777777777>>>>' +this.limit)
  
 if(!this.limit){
 
 this._dl_start[0]=[0,this._dloadSize];
 var loader = new dtmlXMLLoaderObject(this._askRealRows2,this);
 if(this._dloadStr)
 		loader.loadXMLString(this._dloadStr);
 else
 
 

 	
   var obj = this;
   var scrollTop = obj.objBox.scrollTop;
   	
   	
   var callBakFun =function(){
					obj.setSizes();	
					obj.objBox.scrollTop = scrollTop;  
//					obj.posStart = null;   		  
					//	obj.posCount = null;
//					obj.scrollTop = null;			  
					//	obj.fromEditRowId = null;  
//					if(obj.fromEditRowId > 0){
							window.setTimeout(function(){obj.selectRow(obj.getRowById(obj.fromEditRowId),true);obj.fromEditRowId = null;},1000);						
//					}
					//	obj.xmlLoader.waitCall=function(){obj.selectRow(obj.getRowById(obj.fromEditRowId),true);};
					return; 
 		} ; 	

   if(afterCall){
   	  	afterCall = function(){
   	  		afterCall();
   	  		callBakFun();
   	  				}
   }else{
   		afterCall = callBakFun;
   		}
	
  	if(afterCall) loader.waitCall=afterCall;
 
 



 		loader.loadXML(this._dload+((this._dload.indexOf("?")!=-1)?"&":"?")+"posStart="+0+"&total_count="+0+"&scrollTop="+0+"&sn="+(new Date()).valueOf());
      
      
      

//	 this.posStart2 = 0;
//	 this.count2 = 0;
//	 this.scrollTop2 = 0;
//
//
//   var obj = this;
//   var callBakFun =function(){
//					obj.setSizes();	
//					obj.objBox.scrollTop = 0;  
//					obj.posStart = null;   		  
//					//	obj.posCount = null;
//					obj.scrollTop = null;			  
//					//	obj.fromEditRowId = null;  
//					if(obj.fromEditRowId > 0){
//							window.setTimeout(function(){obj.selectRow(obj.getRowById(obj.fromEditRowId),true);obj.fromEditRowId = null;},1000);						
//					}
//					//	obj.xmlLoader.waitCall=function(){obj.selectRow(obj.getRowById(obj.fromEditRowId),true);};
//					return; 
// 		} ; 
//
//
//   if(afterCall){
//   	  	afterCall = function(){
//   	  		afterCall();
//   	  		callBakFun();
//   	  				}
//   }else{
//   		afterCall = callBakFun;
//   		}  


//  	if(afterCall) loader.waitCall=afterCall;
  	
  	
	
  	
 		return true;
}
 if(this.posStart)  pos = parseInt(this.posStart);
 var gi=pos||Math.floor(this.objBox.scrollTop/this._srdh);
 if((this._dom_limit)&&(this.obj._rowslength()>this._dom_limit))
{
 
 
 this._simplifyDom(gi,gi+this._dloadSize);
 
 
}
 
 if(gi>(this.limit-this._dloadSize))gi=this.limit-this._dloadSize;

 var size=gi+this._dloadSize;
 if(size>this.limit)size=this.rowsCol.length;

 for(var j=gi;j<size;j++)
 if((!this.rowsCol[j])||(this.rowsCol[j]._rLoad)||(this.rowsCol[j]._sRow)){
 if(this.rowsBuffer[1][j])
{
 this._addFromBufferSR(j);
}
 else
{
 

 
 if(this._dpref){
 start=this._checkPref(gi,-1);
 count=this._checkPref(size,1)-start;
}
 else
 
 
{count=size-gi;start=gi;}


 this._dl_start[start]=[gi-start,size-gi];
 
 var scrollTop =  this.objBox.scrollTop;

 if(this.posStart) scrollTop =this.scrollTop;
 

// count = mygrid.posCount||count;
  if(this.posStart && start < 0)  start = 0;
  
  

  
 var loader = new dtmlXMLLoaderObject(this._askRealRows2,this);
 
 
	this.posStart2 = start+count;
	this.count2 = this._limitC;
	this.scrollTop2 = scrollTop;


 if(this.posStart){
 	
   var obj = this;

   var callBakFun =function(){
					obj.setSizes();	
					obj.objBox.scrollTop = scrollTop;  
					obj.posStart = null;   		  
					//	obj.posCount = null;
					obj.scrollTop = null;			  
					//	obj.fromEditRowId = null;  
					
					if(obj.fromEditRowId > 0){
							window.setTimeout(function(){obj.selectRow(obj.getRowById(obj.fromEditRowId),true);obj.fromEditRowId = null;},1000);						
					}
					//	obj.xmlLoader.waitCall=function(){obj.selectRow(obj.getRowById(obj.fromEditRowId),true);};
					return; 
 		} ; 	

   if(afterCall){
   	  	afterCall = function(){
   	  		afterCall();
   	  		callBakFun();
   	  				}
   }else{
   		afterCall = callBakFun;
   		}
	
 }
//   if(afterCall)this.waitCall=afterCall;
    if(afterCall) loader.waitCall=afterCall;

//   alert('start'+start);
//    alert('scrollTop'+scrollTop);
   loader.loadXML(this._dload+((this._dload.indexOf("?")!=-1)?"&":"?")+"posStart="+start+"&count="+count+"&total_count="+ this._limitC +"&scrollTop="+ scrollTop +"&sn="+(new Date()).valueOf());
 
//   this.posStart = start;
//   this.total_count = this._limitC;
//   this.scrollTop = scrollTop;

// loader.loadXML(this._dload+((this._dload.indexOf("?")!=-1)?"&":"?")+"posStart="+start+"&total_count="+ this._limitC +"&scrollTop="+scrollTop+"&sn="+(new Date()).valueOf());
//   if(afterCall) loader.waitCall=afterCall;
   
  
 
 if(this.onLS)this.onXLS(this);
// if(afterCall) loader.waitCall=afterCall;
 return;
}
}
}


 dhtmlXGridObject.prototype._askRealRows2=function(obj,xml,c,d,e){

// if(!xml) return false;
 var top=e.getXMLTopNode("rows");

 var rows=e.doXPath("//rows/row",top);
 var z_t=top.getAttribute("total_count");
 if((z_t)&&(!obj._limitC)){
 obj._limitC=obj.limit=parseInt(z_t);
 
}

 if(obj._initDrF){
 
 
 var hheadCol = e.doXPath("//rows/head",top);
 
// alert(hheadCol.length);
 if(hheadCol.length)
 obj._parseHead(hheadCol);
 
 
 obj._initD();
}

 var j=parseInt(top.getAttribute("pos"))||0;
 var llim=(obj._dl_start[j]||[0])[0];
 var tlim=llim+(obj._dl_start[j]||[0,rows.length])[1];

 
 
 if(!obj.limit){
 obj.limit=rows.length;
 obj._fastAddRowSpacer(0,obj.limit*obj._srdh);
 if(obj._ahgr)window.setTimeout(function(){obj._askRealRows();},1);
}
 
 

 for(var i=0;i<rows.length;i++){
 
 
 if((i<llim)||(i>tlim))
{
 if(!_isKHTML){
 obj.rowsBuffer[1][j+i]=rows[i];
 obj.rowsBuffer[0][j+i]=rows[i].getAttribute("id");
}
}
 else
 
 
{
 if((!obj.rowsCol[i+j])||(obj.rowsCol[i+j]._sRow))
 obj._splitRowAt(i+j);

 if(obj.rowsCol[i+j]._rLoad){
 
 if(obj._cssEven){
 if((j+i)%2==1)obj.rowsCol[i+j].className=obj._cssUnEven;
 else obj.rowsCol[i+j].className=obj._cssEven;
}
 obj._fillRowFromXML(obj.rowsCol[i+j],rows[i],-1);

 obj.rowsCol[i+j]._rLoad=false;
 obj.changeRowId(obj.rowsCol[i+j].idd,rows[i].getAttribute("id"));
}
}
}

 if(obj.onXLE)obj.onXLE(this,tlim-llim);
}
 
 dhtmlXGridObject.prototype._splitRowAt=function(ind){
 var id='temp_dLoad_'+this._dInc;
 this._dInc++;
 var z=this.rowsCol[ind];
 if(!z)
{
 
 var ind2=this._findSParent(ind);


 var delta=ind2[1]-(ind-ind2[0])*this._srdh;
 this._fixHeight(this.rowsCol[ind2[0]],delta);


 var z2=this._fastAddRow(id,ind,true,ind2[0])
 z2._sRow=true;

 this._fixHeight(z2,-1*((ind2[1]-(ind-ind2[0])*this._srdh)-this._srdh));
 return this._splitRowAt(ind);
}
 else
 if(z._sRow){
 
 if((this.rowsBuffer[1][ind])&&(this.rowsBuffer[1][ind].tagName=="TR"))
(this._fastAddRow(id,ind,true,null,this.rowsBuffer[1][ind]))._rLoad=false;
 else
(this._fastAddRow(id,ind,true))._rLoad=true;
 this.rowsCol[ind+1]=z;
 this._fixHeight(z,this._srdh);
 if(ind==0)this.setSizes();
}

}
 
 dhtmlXGridObject.prototype._findSParent=function(ind){
 for(var i=ind-1;i>=0;i--){
 if(this.rowsCol[i]){
 return [i,(parseInt(this.rowsCol[i].style.height))];
}
}
}
 
 dhtmlXGridObject.prototype._fixHeight=function(z,delta){
 var x=parseInt(z.style.height||this._srdh)-delta;
 if(x==this._srdh){z._sRow=false;z._rLoad=true;}

 z.style.height=x+"px";
 var n=z.childNodes.length;
 for(var i=0;i<n;i++)
 z.childNodes[i].style.height=x+"px";
}
 
 dhtmlXGridObject.prototype._fastAddRowSpacer=function(ind,height){
 var id='temp_dLoad_'+this._dInc;
 this._dInc++;

 var z=this._fastAddRow(id,ind);
 z.style.height=height+"px";
 var n=z.childNodes.length;
 for(var i=0;i<n;i++)
 z.childNodes[i].style.height=height+"px";

 z._sRow=true;
}


 
 dhtmlXGridObject.prototype._fastAddRow=function(id,ind,nonshift,ind2,z){
 var z=z||this._prepareRow(id);


 if(((ind2)||(ind2=="0"))&&(this.rowsCol[ind2].nextSibling))
 this.rowsCol[ind2].parentNode.insertBefore(z,this.rowsCol[ind2].nextSibling);
 else
{
 if((ind==this.limit)||(this.obj._rowslength()==0)||(!this.rowsCol[ind])){
 if(_isKHTML)
 this.obj.appendChild(z);
 else{
 if(!this.obj.firstChild)
 this.obj.appendChild(document.createElement("TBODY"));
 this.obj.childNodes[0].appendChild(z);
}
}
 else
 this.rowsCol[ind2||ind].parentNode.insertBefore(z,this.rowsCol[ind]);
}


 this.rowsAr[id] = z;
 if(!nonshift)
 this.rowsCol._dhx_insertAt(ind,z);
 else
 this.rowsCol[ind]=z;

 return z;
};

 
dhtmlXGridObject.prototype.getAllItemIds = function(separator){
 var ar = new Array(0)
 var z=this.getRowsNum();
 for(i=0;i<z;i++)
 if((this.rowsCol[i])&&(!this.rowsCol[i]._sRow)&&(!this.rowsCol[i]._rLoad))
 ar[ar.length]=this.rowsCol[i].idd;
 else if(this.rowsBuffer[1][i])
 ar[ar.length]=this.rowsBuffer[0][i];

 return ar.join(separator||",")
}

 
dhtmlXGridObject.prototype._insertRowAt_WSRDA = function(r,ind,skip){
 if(ind<0)ind=this.rowsBuffer[0].length;
 if((arguments.length<2)||(ind===window.undefined))
 ind = this.rowsBuffer[0].length 
 else{
 if(ind>this.rowsBuffer[0].length)
 ind = this.rowsBuffer[0].length;
}

 var ind2=this.rowsBuffer[0][ind]||this.rowsCol[ind].idd;
 this.getRowById(ind2);


 if(!skip)
 if(ind==this.rowsBuffer[0].length){
 if(_isKHTML)
 this.obj.appendChild(r);
 else{
 this.obj.firstChild.appendChild(r);
}
 this.rowsBuffer[0][ind]=r.idd;
 this.rowsBuffer[1][ind]=null;
 ind2=ind;
}
 else
{
 if(!this.rowsCol[ind])
 ind2=(this._findSParent(ind)[0]);
 else ind2=ind;
 this.rowsCol[ind2].parentNode.insertBefore(r,this.rowsCol[ind2]);
 this.rowsBuffer[0]._dhx_insertAt(ind,r.idd);
 this.rowsBuffer[1]._dhx_insertAt(ind,null);
}

 this.limit+=1;
 this.rowsAr[r.idd] = r;
 this.rowsCol._dhx_insertAt(ind2,r);

 if(this._cssEven){
 if(ind%2==1)r.className+=" "+this._cssUnEven;
 else r.className+=" "+this._cssEven;

 if(ind!=(this.rowsCol.length-1))
 this._fixAlterCss(ind+1);
}

 this.doOnRowAdded(r);
 if((this.math_req)&&(!this._parsing_)){
 for(var i=0;i<this.hdr.rows[0].cells.length;i++)
 this._checkSCL(r.childNodes[i]);
 this.math_req=false;
}
 return r;
}
 
dhtmlXGridObject.prototype.deleteRow_WSRDA = function(row_id,node){
 var ind=-1;
 var fixind=null;
 if(this.rowsAr[row_id]){
 ind=this.rowsCol._dhx_find(this.rowsAr[row_id]);
 this.deleteRow_WSRD(row_id,node);
}
 if(ind<0){
 var ind=this.rowsBuffer[0]._dhx_find(row_id);
 if(ind>-1)fixind=this.rowsCol[this._findSParent(ind)[0]];
}

 if(ind>-1)
{
 this.rowsBuffer[0]._dhx_delAt(ind);
 this.rowsBuffer[1]._dhx_delAt(ind);
 this.limit-=1;
 if(fixind)this._fixHeight(fixind,this._srdh);
}
 return true;
}



