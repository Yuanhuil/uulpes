/**
 * @license Copyright (c) 2003-2015, CKSource - Frederico Knabben. All rights reserved.
 * For licensing, see LICENSE.md or http://ckeditor.com/license
 */

CKEDITOR.editorConfig = function( config ) {
	 config.language = 'zh-cn';
	 config.height='460px'
     config.removePlugins='elementspath';
	 //config.toolbarCanCollapse = true;
	 config.toolbarGroups = [
	                        { name: 'editing', groups: [ 'find', 'selection', 'spellchecker', 'editing' ] },
	                        { name: 'basicstyles', groups: [ 'basicstyles', 'cleanup' ] },
	                        { name: 'paragraph', groups: [ 'list', 'indent', 'blocks', 'align', 'bidi', 'paragraph' ] },
	                 		{ name: 'document', groups: [ 'mode', 'document', 'doctools' ] },
	                 		{ name: 'clipboard', groups: [ 'clipboard', 'undo' ] },
	                 		{ name: 'links', groups: [ 'links' ] },
	                 		{ name: 'styles', groups: [ 'styles' ] },
	                 		{ name: 'colors', groups: [ 'colors' ] },
	                 		{ name: 'tools', groups: [ 'tools' ] },
	                 		{ name: 'insert', groups: [ 'insert' ] },
	                 	];

	 config.removeButtons = 'Source,Save,NewPage,Preview,Print,Templates,Find,Replace,SelectAll,Scayt,Form,Checkbox,TextField,Radio,Textarea,Select,Button,ImageButton,HiddenField,About,CreateDiv,BidiLtr,BidiRtl,Language,Iframe';

	                 	// Set the most common block elements.
	 config.format_tags = 'p;h1;h2;h3;pre';

	                 	// Simplify the dialog windows.
	 config.removeDialogTabs = 'image:advanced;link:advanced';
	 config.font_names='宋体/宋体;黑体/黑体;仿宋/仿宋;楷体/楷体;隶书/隶书;幼圆/幼圆;微软雅黑/微软雅黑;'+ config.font_names;
	 config.extraPlugins = 'lineheight';
	 config.filebrowserImageUploadUrl="http://localhost:8080/pes/util/commonAttachment/onlySave.do";
	 config.resize_enabled = false;
};
