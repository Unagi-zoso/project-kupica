function submitDeleteForm(event) {
    event.preventDefault(); // 폼 제출 기본 동작 막기

    var deletePostId = document.getElementById('delete-post-id');
    var deletePasswordInput = document.getElementById("delete-password-input");

    var inputId = deletePostId.value;

    // Fetch 요청 보내기

    fetch('posts/' + inputId + '/delete', {
        method: 'DELETE',
        headers: {
            'Authorization': deletePasswordInput.value
        }
    })
        .then(function(response) {
            if (response.ok) {
                resetModalForm("delete");
                const post_to_delete = document.getElementById("card" + inputId);
                const parent_ele = document.querySelector("#scroll-row");
                parent_ele.removeChild(post_to_delete);
            } else {
                resetModalForm("delete");
                response.json().then(errorData => {
                    alert(errorData.message)
                })
            }
        })
        .catch(error => {
            alert(error.value)
        })
}

