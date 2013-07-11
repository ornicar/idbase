$(function() {
  $(".alert").alert();

  $('form.delete').submit(function() {
    return confirm($(this).attr('title') + " ? ");
  });

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
});
