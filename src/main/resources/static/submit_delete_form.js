function submitDeleteForm(event) {
    event.preventDefault(); // 폼 제출 기본 동작 막기

    var deletePostId = document.getElementById('delete-post-id');
    var deletePasswordInput = document.getElementById("delete-password-input");

    var inputId = deletePostId.value;
    var deleteFormData = new FormData();
    deleteFormData.append('id', inputId);
    deleteFormData.append('password', deletePasswordInput.value);

    // Fetch 요청 보내기

    fetch('post/delete', {
        method: 'POST',
        body: deleteFormData
    })
        .then(function(response) {
            if (response.ok) {
                resetModalForm("delete");
                const post_to_delete = document.getElementById("card" + inputId);
                const parent_ele = document.querySelector("#scroll-row");
                parent_ele.removeChild(post_to_delete);
            } else {
                alert('비밀번호를 확인해주세요!');
                resetModalForm("delete");
            }
        }) .catch(reason => {
        console.error(reason);
    })
}

