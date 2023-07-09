function previewImage(event, id) {
  var input = event.target;
  var preview = document.querySelector("#"+id);
  if (input.files && input.files[0]) {
    var reader = new FileReader();
    reader.onload = function (e) {
      preview.innerHTML =
        '<img src="' + e.target.result + '" class="img-fluid">';
      var image = new Image();
      image.src = e.target.result;
      image.onload = function () {
        var canvas = document.createElement("canvas");
        var context = canvas.getContext("2d");
        canvas.width = imgWidth = image.width;
        canvas.height = imgHeight = image.height;
        context.drawImage(image, 0, 0);
        imageData = context.getImageData(0, 0, image.width, image.height).data;
      };
    };
    reader.readAsDataURL(input.files[0]);
  } else {
    preview.innerHTML = "";
    imageData = null;
  }
}

document.querySelector("#image-chooser").onchange = function() {
  previewImage(event, "preview-image");
}

document.querySelector("#update-image-chooser").onchange = function() {
  previewImage(event, "update-preview-image");
}
