function fileSelectionWithValidation(event) {
    var fileInput = event.target;
    var fileInfoElement = document.getElementById('file-info');

    if (fileInput.files.length > 0) {
        var selectedFile = fileInput.files[0];
        var allowedExtensions = ['.jpg', '.png', '.gif', '.jpeg'];
        var maxSizeInBytes = 10 * 1024 * 1024; // 10MB

        var fileExtension = selectedFile.name.split('.').pop().toLowerCase();
        var fileSizeInBytes = selectedFile.size;

        if (!allowedExtensions.includes('.' + fileExtension)) {
            fileInfoElement.textContent = '선택한 파일의 확장자가 허용되지 않습니다.';
            fileInput.value = '';
            return;
        }

        if (fileSizeInBytes > maxSizeInBytes) {
            fileInfoElement.textContent = '선택한 파일의 크기가 허용 범위를 초과합니다.';
            fileInput.value = '';
            return;
        }

        // 선택한 파일 정보를 표시
        fileInfoElement.textContent = '선택한 파일: ' + selectedFile.name + ' (' + selectedFile.type + ', ' + selectedFile.size + ' bytes)';
    } else {
        fileInfoElement.textContent = '파일이 선택되지 않았습니다.';
    }
}