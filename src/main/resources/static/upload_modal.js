document.getElementById("modal_open_btn").onclick = function() {
    document.getElementById("modal-upload").style.display="flex"
}

document.getElementById("modal_close_btn").onclick = function() {
    document.getElementById("modal-upload").style.display="none";
}

document.getElementById("modal-upload").addEventListener("click", e => {
    const evTarget = e.target
    if(evTarget.classList.contains("modal-overlay")) {
        document.getElementById("modal-upload").style.display = "none"
    }
})

document.getElementById("update-modal-close-btn").addEventListener("click", e => {
    resetModalUpdateForm();
});

function resetModalUpdateForm() {
    document.getElementById("modal-update").style.display="none";
    document.getElementById('update-preview-image').innerHTML = "";
    document.getElementById('update-file-info').innerHTML = "";
    document.getElementById('update-form').reset();
}