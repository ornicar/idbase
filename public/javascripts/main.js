$(function() {
  $(".alert").alert();

  $('form.delete').submit(function() {
    return confirm($(this).attr('title') + " ? ");
  });

  $("input.tm-input").each(function() {
    var input = $(this);
    input.tagsManager({
      prefilled: input.val()
    });
  });
});
