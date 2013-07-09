$(function() {
  $(".alert").alert()

  $('form.delete').submit(function() {
    return confirm($(this).attr('title') + " ? ");
  });
});
