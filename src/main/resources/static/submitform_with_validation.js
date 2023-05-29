function submitForm(event) {
    event.preventDefault(); // 폼 제출 기본 동작 막기

    var fileInput = document.getElementById('fileInput');
    var selectedFile = fileInput.files[0];
    var allowedExtensions = ['.jpg', '.png', '.gif', '.jpeg'];

    if (selectedFile) {
        var fileExtension = selectedFile.name.split('.').pop().toLowerCase();

        if (!allowedExtensions.includes('.' + fileExtension)) {
            alert('선택한 파일의 확장자가 허용되지 않습니다.');
            return;
        }

        var formData = new FormData();
        formData.append('file', selectedFile);

        // Fetch 요청 보내기
        fetch('/post/upload', {
            method: 'POST',
            body: formData
        })
            .then(function(response) {
                if (response.ok) {
                    alert('파일 업로드 성공!');
                } else {
                    alert('파일 업로드 실패!');
                }
            })
            .catch(function(error) {
                alert('파일 업로드 중 오류가 발생했습니다.');
                console.error(error);
            });
    }
}