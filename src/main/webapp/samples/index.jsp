<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Combo Multi Kolom</title>


<script src="../static/ext/4.0/ext-all.js" type="text/javascript"></script>
<link href="../static/ext/4.0/ext-all.css" type="text/css" rel="stylesheet" />


<script type="text/javascript">

Ext.require([
    'Ext.window.Window',
    'Ext.tab.*',
    'Ext.toolbar.Spacer',
    'Ext.layout.container.Card',
    'Ext.layout.container.Border'
]);

Ext.onReady(function(){
    var constrainedWin, constrainedWin2;
    
    Ext.util.Region.override({
        colors: ['red', 'orange', 'yellow', 'green', 'blue', 'indigo', 'violet'],
        nextColor: 0,
        show: function(){
            var style = {
                display: 'block',
                position: 'absolute',
                top: this.top + 'px',
                left: this.left + 'px',
                height: ((this.bottom - this.top) + 1) + 'px',
                width: ((this.right - this.left) + 1) + 'px',
                opacity: 0.3,
                'pointer-events': 'none',
                'z-index': 9999999
            };
            if (!this.highlightEl) {
                style['background-color'] = this.colors[this.nextColor];
                Ext.util.Region.prototype.nextColor++;
                this.highlightEl = Ext.getBody().createChild({
                    style: style
                });
                if (this.nextColor >= this.colors.length) {
                    this.nextColor = 0;
                }
            } else {
                this.highlightEl.setStyle(style);
            }
        },
        hide: function(){
            if (this.highlightEl) {
                this.highlightEl.setStyle({
                    display: 'none'
                });
            }
        }
    });

    var win2 = Ext.create('widget.window', {
        height: 200,
        width: 400,
        x: 450,
        y: 450,
        title: 'Constraining Window, plain: true',
        closable: false,
        plain: true,
        layout: 'fit',
        items: [constrainedWin = Ext.create('Ext.Window', {
            title: 'Constrained Window',
            width: 200,
            height: 100,

            // Constraining will pull the Window leftwards so that it's within the parent Window
            x: 1000,
            y: 20,
            constrain: true,
            layout: 'fit',
            items: {
                border: false
            }
        }), constrainedWin2 = Ext.create('Ext.Window', {
            title: 'Header-Constrained Win',
            width: 200,
            height: 100,
            x: 120,
            y: 120,
            constrainHeader: true,
            layout: 'fit',
            items: {
                border: false
            }
        }),{
            border: false
        }]
    });
    win2.show();
    constrainedWin.show();
    constrainedWin2.show();

    Ext.create('Ext.Window', {
        title: 'Left Header, plain: true',
        width: 400,
        height: 200,
        x: 10,
        y: 200,
        plain: true,
        headerPosition: 'left',
        layout: 'fit',
        items: {
            border: false
        }
    }).show();

    Ext.create('Ext.Window', {
        title: 'Right Header, plain: true',
        width: 400,
        height: 200,
        x: 450,
        y: 200,
        headerPosition: 'right',
        layout: 'fit',
        items: {
            border: false
        }
    }).show();

    Ext.create('Ext.Window', {
        title: 'Bottom Header, plain: true',
        width: 400,
        height: 200,
        x: 10,
        y: 450,
        plain: true,
        headerPosition: 'bottom',
        layout: 'fit',
        items: {
            border: false
        }
    }).show();
});

</script>
</head>
<body>
Body <div id="grid-example"></div>
 <div id="induk"></div>


<div id='induk'></div>
</body>
</html>