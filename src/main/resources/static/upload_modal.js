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
    resetModalForm("update");
});

document.getElementById("delete-modal-close-btn").addEventListener("click", e => {
    resetModalForm("delete");
});

function resetModalForm(form) {
    document.getElementById("modal-"+form).style.display="none";
    document.getElementById(form+'-preview-image').innerHTML = "";
    document.getElementById(form+'-file-info').innerHTML = "";
    document.getElementById(form+'-form').reset();
}