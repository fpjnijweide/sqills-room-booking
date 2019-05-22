jQuery(document).ready(function($) {
    $(".clickable-row").click(function() {
        console.log("hi");
        window.location = $(this).data("href");
    });
});