function submitUploadForm(event) {
    event.preventDefault(); // 폼 제출 기본 동작 막기

    var fileInput = document.getElementById('image-chooser');
    var selectedFile = fileInput.files[0];
    var allowedExtensions = ['.jpg', '.png', '.gif', '.jpeg'];

    if (selectedFile) {
        var fileExtension = selectedFile.name.split('.').pop().toLowerCase();

        if (!allowedExtensions.includes('.' + fileExtension)) {
            alert('선택한 파일의 확장자가 허용되지 않습니다.');
            return;
        }

        var formData = new FormData();

        var captionInput = document.getElementById("caption-input");
        var passwordInput = document.getElementById("password-input");

        formData.append('file', selectedFile);
        formData.append('caption', captionInput.value);

        // Fetch 요청 보내기
        fetch('posts', {
            method: 'POST',
            headers: {
                'Authorization': passwordInput.value
            },
            body: formData
        })
            .then(function(response) {

                if (response.ok) {
                    resetModalUploadForm();
                } else {
                    response.json().then(errorData => {
                        alert(errorData.message)
                    })
                }
            })
            .catch(error => {
                alert(error.value)
            })

    }
}

function resetModalUploadForm() {
    document.getElementById('modal-upload').style.display = "none";
    document.getElementById('preview-image').innerHTML = "";
    document.getElementById('file-info').innerHTML = "";
    document.getElementById('upload-form').reset();
}