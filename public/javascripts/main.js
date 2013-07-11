$(function() {
  $(".alert").alert();

  $('form.delete').submit(function() {
    return confirm($(this).attr('title') + " ? ");
  });

  $('select').each(function() {
    var select = $(this);
    if (!select.attr('title')) select.attr('title', '-');
    select.addClass('show-tick show-menu-arrow').selectpicker();
  });

  var error = $('.control-group.error');
  if (error.length) {
    setTimeout(function() {
      $.scrollTo(0, error.offset().top - 20);
    }, 200);
  }

  if ($.fn.tagsManager) {
    $("input.tm-input").each(function() {
      var input = $(this);
      if (input.val() == "") input.val(input.closest('form').find('.prefill-notions').val());
      var source = input.closest('form').find('.notions').val().split(',');
      input.tagsManager({
        prefilled: input.val(),
        typeahead: true,
        typeaheadSource: source,
        typeaheadDelegate: {
          matcher: function(item) {
            return item.toLowerCase().indexOf(this.query.toLowerCase()) == 0
          }
        },
        backspace: false
      });
    });
  }

  $.scrollTo = $.fn.scrollTo = function(x, y, options) {
    if (!(this instanceof $)) return $.fn.scrollTo.apply($('html, body'), arguments);

    options = $.extend({}, {
      gap: {
        x: 0,
        y: 0
      },
      animation: {
        easing: 'swing',
        duration: 600,
        complete: $.noop,
        step: $.noop
      }
    }, options);

    return this.each(function() {
      var elem = $(this);
      elem.stop().animate({
        scrollLeft: !isNaN(Number(x)) ? x : $(x).offset().left + options.gap.x,
        scrollTop: !isNaN(Number(y)) ? y : $(y).offset().top + options.gap.y
      }, options.animation);
    });
  };
});
