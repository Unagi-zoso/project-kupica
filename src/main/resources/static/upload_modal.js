document.getElementById("modal_open_btn").onclick = function() {
    document.getElementById("modal").style.display="flex"
}

document.getElementById("modal_close_btn").onclick = function() {
    document.getElementById("modal").style.display="none";
}

document.getElementById("modal").addEventListener("click", e => {
    const evTarget = e.target
    if(evTarget.classList.contains("modal-overlay")) {
        document.getElementById("modal").style.display = "none"
    }
})
