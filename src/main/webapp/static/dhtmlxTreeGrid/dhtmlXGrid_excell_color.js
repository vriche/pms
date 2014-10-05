//color picker
function eXcell_color(cell){
	try {
	    this.cell = cell;
	    this.grid = this.cell.parentNode.grid;
	} catch(e){}
	this.edit = function() {
						this.val = this.getValue();
						this.obj_container = document.createElement('div');
						this.obj_container.onclick=function(e){
							(e||event).cancelBubble=true;
						}
						this.obj_container.style.position = "absolute";
						var arPos = this.grid.getPosition(this.cell);//,this.grid.objBox
						this.obj_container.style.left = arPos[0]-this.grid.objBox.scrollLeft+"px";
						this.obj_container.style.top = arPos[1]+this.cell.offsetHeight-this.grid.objBox.scrollTop+"px";
						document.body.appendChild(this.obj_container);//this.grid.objBox.appendChild(this.obj);
						this.obj = new dhtmlXColorPicker(this.obj_container);
                        this.obj.setColorRGB(this.val);
                        var self = this;
                        this.obj.setOnSelectHandler(function(color){self.editorClose(color)});
	}
	this.editorClose = function(color) {
        this.setValue('#'+color);
        this.grid.editStop();
	}
	this.getValue = function() {
						return this.cell.cur_color;//this.getBgColor()
	}
	this.detach = function() {
					this.obj.close();
                    document.body.removeChild(this.obj_container);
					return this.val!=this.getValue();
	}
}
eXcell_color.prototype = new eXcell;
eXcell_color.prototype.setValue = function(val){
						this.cell.innerHTML = "<div style='width:100%;height:"+(this.cell.offsetHeight-2)+";background-color:"+(val||"")+";border:0px;'>&nbsp;</div>";//this.setBgColor(val)
						this.cell.cur_color = val;

					}
