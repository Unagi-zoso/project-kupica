function submitUpdateForm(event) {
    event.preventDefault(); // 폼 제출 기본 동작 막기

    var updateFileInput = document.getElementById('update-image-chooser');
    var updatePostId = document.getElementById('update-post-id');
    var updateCaptionInput = document.getElementById("update-caption-input");
    var updatePasswordInput = document.getElementById("update-password-input");

    var isExistFile = false;

    var inputId = updatePostId.value;
    var inputCaption = updateCaptionInput.value;

    var updateFormData = new FormData();
    updateFormData.append('id', inputId);
    updateFormData.append('caption', inputCaption);
    updateFormData.append('password', updatePasswordInput.value);

    var selectedUpdateFile = null;
    if (updateFileInput.files.length > 0) {
        selectedUpdateFile = updateFileInput.files[0];
        var allowedExtensions = ['.jpg', '.png', '.gif', '.jpeg'];
        var fileExtension = selectedUpdateFile.name.split('.').pop().toLowerCase();

        if (!allowedExtensions.includes('.' + fileExtension)) {
            alert('선택한 파일의 확장자가 허용되지 않습니다.');
            return;
        }
        isExistFile = true;
        updateFormData.append('file', selectedUpdateFile);
    }

    // Fetch 요청 보내기


    fetch('posts/'+inputId, {
        method: 'PATCH',
        body: updateFormData
    })
        .then(function(response) {

            if (response.ok) {
                resetModalForm("update");
                document.getElementById("caption"+inputId).innerText = inputCaption;
                if (isExistFile) {
                    var reader = new FileReader();
                    reader.onload = function(e) {
                        result = e.target.result;
                        document.getElementById("img"+inputId).src = result;
                    }

                    reader.readAsDataURL(selectedUpdateFile);
                }
            } else {
                alert('파일 수정 실패!');
            }
        }) .catch(reason => {
        console.error(reason);
    })
}

