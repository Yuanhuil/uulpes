(function ($) {

    "use strict"; // jshint ;_;


    /* Paginator PUBLIC CLASS DEFINITION
     * ================================= */

    /**
     * Boostrap Paginator Constructor
     *
     * @param element element of the paginator
     * @param options the options to config the paginator
     *
     * */
    var JstlPaginator = function (element, options) {
        this.init(element, options);
    },
        old = null;
   
    JstlPaginator.prototype = {

        /**
         * Initialization function of the paginator, accepting an element and the options as parameters
         *
         * @param element element of the paginator
         * @param options the options to config the paginator
         *
         * */
        init: function (element, options) {

            this.$element = $(element);

            this.currentPage = 1;

            this.lastPage = 1;

            this.setOptions(options);

            this.initialized = true;
            
        },

        /**
         * Update the properties of the paginator element
         *
         * @param options options to config the paginator
         * */
        setOptions: function (options) {

            this.options = $.extend({}, (this.options || $.fn.jstlPaginator.defaults), options);

            this.totalPages = parseInt(this.options.totalPages, 10);  //setup the total pages property.


            this.listen();

            //render the paginator
            this.render();

        },

        /**
         * Sets up the events listeners. Currently the pageclicked and pagechanged events are linked if available.
         *
         * */
        listen: function () {

            this.$element.off("page-clicked");

            if (typeof (this.options.onPageClicked) === "function") {
                this.$element.bind("page-clicked", this.options.onPageClicked);
            }

            this.$element.bind("page-clicked", this.onPageClicked);
        },


        /**
         *
         *  Destroys the paginator element, it unload the event first, then empty the content inside.
         *
         * */
        destroy: function () {

            this.$element.off("page-clicked");

            this.$element.removeData('jstlPaginator');

            this.$element.empty();

        },




        /**
         * Internal on page item click handler, when the page item is clicked, change the current page to the corresponding page and
         * trigger the pageclick event for the listeners.
         *
         *
         * */
        onPageItemClicked: function (event) {

            var page = event.data.page;
            this.$element.trigger("page-clicked", [event, page]);

        },

        onPageClicked: function (event, originalEvent, page) {
        },

        render: function(){
        	this.$element.addClass("pageNav");
        	this.$element.append(this.renderPages());
        },
        
        /**
         * Renders the paginator according to the internal properties and the settings.
         * */
        renderPages: function () {
            var sdiv = $("<div></div>"),
            	firstPageButton = $("<input type='button'></input>"),
                lastPageButton = $("<input type='button'></input>"),
            	prePageButton= $("<input></input>"),
            	showText = $("<b></b>"),
            	nextButton= $("<input></input>");
            if(parseInt(this.options.totalPages) === 0) return sdiv;
            var prepage = parseInt(this.options.currentPage) == 1 ? 0 : (parseInt(this.options.currentPage) - 1),
            	nextpage = parseInt(this.options.totalPages) <= parseInt(this.options.currentPage) ? this.options.currentPage : (parseInt(this.options.currentPage) + 1);
            var firstpage  = 1, lastpage = this.options.totalPages;
            
            firstPageButton.addClass("button-normal white");
            firstPageButton.attr("value","首页");
            lastPageButton.addClass("button-normal white");
            lastPageButton.attr("value","尾页");
            showText.append("第").append(this.options.currentPage).append("页/共").append(this.options.totalPages).append("页");
            if(this.options.showtotalPage){
        		sdiv.append(this.buildTotalPageItem(this.options.totalNumbers));
        	}
            if(prepage > 0){
            	 prePageButton.addClass("button-normal white");
                 prePageButton.attr("value","上一页");
                 prePageButton.attr("type","button");
                 prePageButton.on("click", null, {page: prepage}, $.proxy(this.onPageItemClicked, this));
                 firstPageButton.on("click", null, {page: firstpage}, $.proxy(this.onPageItemClicked, this));
                 sdiv.append(firstPageButton);
              	 sdiv.append(prePageButton);
            }
           
            if(parseInt(this.options.totalPages) !== parseInt(this.options.currentPage)){
            	nextButton.addClass("button-normal white");
                nextButton.attr("value","下一页");
                nextButton.attr("type","button");
                nextButton.on("click", null, {page: nextpage}, $.proxy(this.onPageItemClicked, this));
                lastPageButton.on("click", null, {page: lastpage}, $.proxy(this.onPageItemClicked, this));
                sdiv.append(nextButton);
                sdiv.append(lastPageButton);
            }
           
          
           
            sdiv.append(showText);
            return sdiv;
        },
        /**
         * build total numbers
         * */
        buildTotalPageItem:function(totalNumbers){
        	var totalPageItem =$("<b></b>");
        	totalPageItem.append("共").append(totalNumbers).append("条 ");
        	return totalPageItem;
        }
    };


    /* TYPEAHEAD PLUGIN DEFINITION
     * =========================== */

    old = $.fn.jstlPaginator;

    $.fn.jstlPaginator = function (option) {

        var args = arguments,
            result = null;

        $(this).each(function (index, item) {
            var $this = $(item),
                data = $this.data('jstlPaginator'),
                options = (typeof option !== 'object') ? null : option;

            if (!data) {
                data = new JstlPaginator(this, options);

                $this = $(data.$element);

                $this.data('jstlPaginator', data);

                return;
            }

            if (typeof option === 'string') {

                if (data[option]) {
                    result = data[option].apply(data, Array.prototype.slice.call(args, 1));
                } else {
                    throw "Method " + option + " does not exist";
                }

            } else {
                result = data.setOptions(option);
            }
        });

        return result;

    };


    $.fn.jstlPaginator.defaults = {
    	showtotalPage:false,
        showgotoPage:true,
        currentPage: 1,
        totalPages: 1,
        totalNumbers:1,
        pageUrl: function (type, page, current) {
            return null;
        },
        onPageClicked: null,
    };

    $.fn.jstlPaginator.Constructor = JstlPaginator;



}(window.jQuery));
