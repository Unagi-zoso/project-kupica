let isFetching = false;
const defaultPaginationSize = 6;
const URL = "paging";
const DownloadURL = "download";
const DeleteURL = "post/delete";
let lastId = 0;

const drawList = (DATA) => {

    DATA.forEach((item, index) => {

        const { post_id, source, download_key, caption } = item;

        const DIV_CARD = document.createElement('div');
        DIV_CARD.setAttribute("class", "col-md-6");
        DIV_CARD.setAttribute("class", "mb-4");
        DIV_CARD.setAttribute("class", "card");
        DIV_CARD.setAttribute("style", "width: 38rem");

        const DIV_IMG_FRAME = document.createElement('div');
        DIV_IMG_FRAME.setAttribute("class", "bg-image");
        DIV_IMG_FRAME.setAttribute("class", "hover-overlay");
        DIV_IMG_FRAME.setAttribute("class", "ripple");
        DIV_IMG_FRAME.setAttribute("class", "shadow-1-strong");
        DIV_IMG_FRAME.setAttribute("class", "rounded");
        DIV_IMG_FRAME.setAttribute("class", "mb-4");
        DIV_IMG_FRAME.setAttribute("data-mdb-ripple-color", "light");

        const IMG_ELE = document.createElement('img');
        IMG_ELE.setAttribute("class", "w-100");
        IMG_ELE.setAttribute("src", source);

        const A_CARD_BACKGROUND = document.createElement('a');
        A_CARD_BACKGROUND.setAttribute("href", "몰라유...");

        const DIV_ELE_3 = document.createElement('div');
        DIV_ELE_3.setAttribute("class", "mask");
        DIV_ELE_3.setAttribute("style", "background-color: rgba(251, 251, 251, 0.2)");

        const DIV_ELE_4 = document.createElement('div');
        DIV_ELE_3.setAttribute("class", "card-body");
        DIV_ELE_3.setAttribute("style", "background-color: rgba(251, 251, 251, 0.2)");

        const P_ELE = document.createElement('p');
        P_ELE.innerText = caption;

        const A_ELE_2 = document.createElement('a');
        const btn_id = "btn-download" + post_id;
        A_ELE_2.setAttribute("class", "btn");
        A_ELE_2.setAttribute("class", "btn-info");
        A_ELE_2.setAttribute("class", "btn-rounded");
        A_ELE_2.setAttribute("id", btn_id);
        A_ELE_2.setAttribute("role", "button");

        A_ELE_2.addEventListener("click", function () { downloadImg(download_key) });

        A_ELE_2.innerText = "Download";

        const A_ELE_3 = document.createElement('a');
        const delete_btn_id = "btn-delete" + post_id;
        A_ELE_3.setAttribute("class", "btn");
        A_ELE_3.setAttribute("class", "btn-info");
        A_ELE_3.setAttribute("class", "btn-rounded");
        A_ELE_3.setAttribute("id", delete_btn_id);
        A_ELE_3.setAttribute("role", "button");

        A_ELE_3.innerText = "Delete";

        const DIV_MOD_ELE_0 = document.createElement('div')
        DIV_MOD_ELE_0.setAttribute("class", "modal-delete");
        const delete_modal_id = "modal-delete" + post_id;

        const DIV_MOD_ELE_1 = document.createElement('div')
        DIV_MOD_ELE_1.setAttribute("id", delete_modal_id);
        DIV_MOD_ELE_1.setAttribute("class", "modal-overlay");

        const DIV_MOD_ELE_2 = document.createElement('div')
        DIV_MOD_ELE_2.setAttribute("class", "modal-window");

        const DIV_MOD_ELE_3 = document.createElement('div')
        DIV_MOD_ELE_3.setAttribute("class", "content");

        const DIV_INPUT_FORM = document.createElement('div')
        DIV_INPUT_FORM.setAttribute("method", "POST");

        const INPUT_PASSWORD = document.createElement('input');
        INPUT_PASSWORD.setAttribute("id", "password" + post_id);
        INPUT_PASSWORD.setAttribute("type", "password");
        INPUT_PASSWORD.setAttribute("placeholder", "password");
        INPUT_PASSWORD.setAttribute("name", "password");

        const BTN_DELETE = document.createElement('button');
        BTN_DELETE.setAttribute("id", "btn-delete" + post_id);
        BTN_DELETE.setAttribute("class", "cta blue");
        BTN_DELETE.innerText = "제거";

        BTN_DELETE.addEventListener("click", function () { deletePost(post_id, document.getElementById("password" + post_id).value); });

        const BTN_CLOSE_DELETE_MODAL = document.createElement('a');
        BTN_CLOSE_DELETE_MODAL.setAttribute("id", "modal_close_btn");
        BTN_CLOSE_DELETE_MODAL.innerText = "창 닫기";

        A_ELE_3.addEventListener("click", function () {
            const evTarget = document.getElementById(delete_modal_id);
            if(evTarget.classList.contains("modal-overlay")) {
                document.getElementById("modal-delete" + post_id).style.display = "block"
            }
        });

        BTN_CLOSE_DELETE_MODAL.addEventListener("click", function () {
            const evTarget = document.getElementById(delete_modal_id);
            if(evTarget.classList.contains("modal-overlay")) {
                document.getElementById("modal-delete" + post_id).style.display = "none"
            }
        });

        DIV_MOD_ELE_0.append(DIV_MOD_ELE_1);
        DIV_MOD_ELE_1.append(DIV_MOD_ELE_2);
        DIV_MOD_ELE_2.append(DIV_MOD_ELE_3);
        DIV_MOD_ELE_3.append(DIV_INPUT_FORM);
        DIV_INPUT_FORM.append(INPUT_PASSWORD);
        DIV_INPUT_FORM.append(BTN_DELETE);
        DIV_INPUT_FORM.append(BTN_CLOSE_DELETE_MODAL);

        if (index === DATA.length - 1) lastId = post_id; // 마지막건 ID 저장

        DIV_ELE_4.append(P_ELE);
        A_CARD_BACKGROUND.append(DIV_ELE_3);
        DIV_IMG_FRAME.append(IMG_ELE, A_CARD_BACKGROUND);
        DIV_CARD.append(DIV_IMG_FRAME, DIV_ELE_4, A_ELE_2, A_ELE_3);
        DIV_CARD.append(DIV_MOD_ELE_0);

        document.querySelector("#scroll-row").append(DIV_CARD);
    });
    isFetching = false; // callback이 끝났으니 isFetching 리셋
};

const getList = () => {
    isFetching = true; // 아직 callback이 끝나지 않았어요!
    fetch(URL, {
        method: "POST",
        headers: {
            'Content-type': 'application/json',
        },
        body: JSON.stringify({
            "lastPageId" : lastId,
            "defaultPageSize" : defaultPaginationSize
        })
    })
        .then(response => response.json())
        .then(drawList)
        .catch((e) => {
            console.log(e);
        });
};

function downloadImg(source) {
    console.log(DownloadURL + "?" + "fileUrl=" + source);
    fetch(DownloadURL + "?" + "fileUrl=" + source, {
        method: "GET",

    }).then(response => response.blob())
        .then(function (response) {
            let link = document.createElement('a');
            link.style.display = 'none';
            document.body.appendChild(link);
            console.log(response);
            link.href = window.URL.createObjectURL(response);
            link.download = source;
            link.click();
        }).catch((e) => {
    });
}

const deletePost = (post_id, password) => {
    isFetching = true; // 아직 callback이 끝나지 않았어요!
    fetch(DeleteURL, {
        method: "POST",
        headers: {
            'Content-type': 'application/json',
        },
        body: JSON.stringify({
            "id" : post_id,
            "password" : password
        })
    })
        .catch((e) => {
            console.log(e);
        });
};

window.addEventListener("scroll", function () {
    const SCROLLED_HEIGHT = window.scrollY;
    const WINDOW_HEIGHT = window.innerHeight;
    const DOC_TOTAL_HEIGHT = document.body.offsetHeight;
    const IS_BOTTOM = (WINDOW_HEIGHT + SCROLLED_HEIGHT > DOC_TOTAL_HEIGHT - 500);

    if (IS_BOTTOM && !isFetching) { // isFetching이 false일 때 조건 추가
        getList();
    }
});

