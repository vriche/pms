/*******************
dhtmlxgrid 列表合并 colspan
如果对 一行进行多次列合并可能有问题
@return 如果 colspan 大于1， true=  合并成功 ，false =取消合并
@author  wdz123@hotmail.com
@date 2009-01-28
example: mygrid.setColspan(1,2,2); mygrid.setColspan(1,6,3);
*/
dhtmlXGridObject.prototype.setColspan = function(row_id, col_ind, colspan){
//setColspan(row_id, col_ind, colspan) [
var tr ;
tr = this.getRowById(row_id);  
if(tr == 'undefined' || tr == null){
return false ;
}
var colspancount = colspan;
var j = tr.childNodes.length ;
//检查边界
     if(colspancount <= 1){
   return false ;  
}
var kk = (col_ind + colspancount );
if(j < kk){
  colspancount = j  -  col_ind;
}
//alert('step 1');
var my = new _dhtmlXGridObjectColSpan();
return my._meregeCells(tr,col_ind,colspancount);
}

//dhtmlXGridObject  列合并内部对象
function _dhtmlXGridObjectColSpan () {
   //判断是否允许列合并
  //decide may merge  cells
  this._canMergeCells = function( tr,col_ind, colspan){
    var  canMerge = true;
var  k ;
var y = (parseInt(col_ind) + parseInt(colspan ));
var i;
i  = col_ind;
while( i <  y && canMerge ){
  if(tr.childNodes[i] && tr.childNodes[i].colSpan){
    k = tr.childNodes[i].colSpan;
if(k>1){
   canMerge  = false;
  // i =  y  +1;
}

if(tr.childNodes[i].style  && tr.childNodes[i].style.display){
    k = tr.childNodes[i].style.display;
if(k=='none' || k == 'NONE'){
   canMerge  = false;
     //  i = y  +1;
}
}

  }
  i ++ ;  
}
//alert('canMerge=' + canMerge)
return canMerge;
  };

   //寻找取消合并的开始位置
  //find  first  cancel merge cell position
  //tr is table  row object
   this._findBeginMergePos  = function(tr,col_ind){
    var i = col_ind;
var flag = true;
var tmp ;
var result  = col_ind;
if(tr.childNodes[col_ind].style && tr.childNodes[col_ind].style.display){
    tmp  = tr.childNodes[col_ind].style.display;
if(tmp=="none" ||tmp == "NONE"){
    //需要找到合并开始点
//need find
}else{
  flag  = false ;
}
}
while(flag && i>=0) {
   if(tr.childNodes[i].colSpan){
      tmp = tr.childNodes[i].colSpan;
  if(tmp>1){   
     result = i;
flag = false;
   }
}
i = i -1;
}
if(flag){   
  result = 0 ;
}
return result;
  };
 
    //寻找取消合并的结束位置
  //cellsCount is the row cells'count
   this._findEndMergePos = function(tr,col_ind,cellsCount){
    var i = col_ind;
var flag = true;
var tmp ;
var result  = col_ind  ;
//alert('_findEndMergePos,col_ind='+col_ind)
if(tr.childNodes[col_ind].style && tr.childNodes[col_ind].style.display){
    tmp  = tr.childNodes[col_ind].style.display;
if(tmp=="none" ||tmp == "NONE"){
    //需要找到合并开始点
}
else{
  flag  = false ; 
}
}

while( flag && i< cellsCount) {
   if(tr.childNodes[i].colSpan){
      tmp = tr.childNodes[i].colSpan;
  if(tmp>1){   
     if(i>col_ind){
       result = i -1 ;
   flag = false;
}
   }
}
i = i + 1;
}
if(flag){   
  result = cellsCount -1  ;
}
return result;
  };
 

//列合并
//@return  true =merged  success ;false = unmerged
  this._meregeCells = function(tr,col_ind,colspancount){   
    //alert('step 2');
    var tmp;
    var canMerge = this._canMergeCells(tr,col_ind,colspancount);
    if(!canMerge){
      //本次操作应该是取消合并
  //获得取消合并开始位置   
  var beginFindPos  = this._findBeginMergePos(tr,col_ind);
  //alert('tr.childNodes.length' +tr.childNodes.length)
  //获得取消合并结束位置
  var tt = (col_ind+colspancount -1 );
  var endFindPos  = this._findEndMergePos(tr,tt ,tr.childNodes.length);
 
  //alert('beginFindPos='+ beginFindPos +",endFindPos=" + endFindPos)
  for(var i=beginFindPos;i <= endFindPos && i< tr.childNodes.length ; i++){
     tr.childNodes[i].style.display = "";
if(tr.childNodes[i].colSpan){
       tr.childNodes[i].colSpan =1;
}

  }
  return false;
   }
  // alert('tr.childNodes.length---' )  
   //var k;   
   //alert('row col size=' + tr.childNodes.length +",col_ind + colspancount=" +(col_ind + colspancount))
   for (var i = col_ind; i < (col_ind + colspancount  ); i++){
    if(i == col_ind){
   tr.childNodes[col_ind].colSpan = colspancount ;
}else{  
   tr.childNodes[i].style.display = "none";
   //alert('remove')
}
  }
  return true;

}
} 