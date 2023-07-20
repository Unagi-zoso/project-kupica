let isFetching = false;
let isQuerying = false;
const defaultPaginationSize = 6;
const URL_PAGING = "/posts/page";
let lastId = 0;

const drawList = (DATA) => {

    DATA.forEach((item, index) => {

        const { post_id, source, cached_image_url, download_key, caption } = item;

        const DIV_CARD = document.createElement('div');
        DIV_CARD.setAttribute("class", "col-md-6 mb-4 card");
        DIV_CARD.setAttribute("style", "width: 38rem");
        DIV_CARD.setAttribute("id", "card" + post_id);

        const DIV_IMG_FRAME = document.createElement('div');
        DIV_IMG_FRAME.setAttribute("class", "bg-image hover-overlay ripple shadow-1-strong rounded mb-4");
        DIV_IMG_FRAME.setAttribute("data-mdb-ripple-color", "light");

        const IMG_ELE = document.createElement('img');
        IMG_ELE.setAttribute("id", "img" + post_id);
        IMG_ELE.setAttribute("src", cached_image_url);

        var img = new Image();
        img.src = cached_image_url;

        const A_CARD_BACKGROUND = document.createElement('a');
        A_CARD_BACKGROUND.setAttribute("href", "몰라유...");

        const DIV_ELE_3 = document.createElement('div');
        DIV_ELE_3.setAttribute("class", "mask");
        DIV_ELE_3.setAttribute("style", "background-color: rgba(251, 251, 251, 0.2)");

        const DIV_ELE_4 = document.createElement('div');
        DIV_ELE_3.setAttribute("class", "card-body");
        DIV_ELE_3.setAttribute("style", "background-color: rgba(251, 251, 251, 0.2)");

        const P_ELE = document.createElement('p');
        P_ELE.setAttribute("id", "caption" + post_id);
        P_ELE.innerText = caption;

        const DIV_ELE_GROUPING_BUTTONS = document.createElement('div');

        const BUTTON_ELE_1 = document.createElement('button');
        const btn_id = "btn-download" + post_id;
        BUTTON_ELE_1.setAttribute("class", "btn btn-outline-primary btn-rounded");
        BUTTON_ELE_1.setAttribute("id", btn_id);

        BUTTON_ELE_1.addEventListener("click", function () { downloadImg(post_id) });

        const BUTTON_ELE_2 = document.createElement('button');
        const update_btn_id = "btn-update" + post_id;
        BUTTON_ELE_2.setAttribute("class", "btn btn-outline-success btn-rounded");
        BUTTON_ELE_2.setAttribute("id", update_btn_id);

        BUTTON_ELE_2.onclick = function() {
            document.getElementById("modal-update").style.display="flex";
            document.getElementById("update-post-id").value = post_id;
            document.getElementById("update-preview-image").innerHTML = '<img src="' + document.getElementById("img"+post_id).src + '" class="img-fluid">';
            document.getElementById("update-caption-input").value = document.getElementById("caption"+post_id).innerText;
        };

        const BUTTON_ELE_3 = document.createElement('button');
        const delete_btn_id = "btn-delete" + post_id;
        BUTTON_ELE_3.setAttribute("class", "btn btn-outline-danger btn-rounded");
        BUTTON_ELE_3.setAttribute("id", delete_btn_id);

        BUTTON_ELE_3.onclick = function() {
            document.getElementById("modal-delete").style.display="flex";
            document.getElementById("delete-post-id").value = post_id;
            document.getElementById("delete-preview-image").innerHTML = '<img src="' + document.getElementById("img"+post_id).src + '" class="img-fluid">';
        };

        DIV_ELE_4.append(P_ELE);
        A_CARD_BACKGROUND.append(DIV_ELE_3);
        DIV_IMG_FRAME.append(IMG_ELE, A_CARD_BACKGROUND);
        DIV_CARD.append(DIV_IMG_FRAME);

        const DIV_GREAT_HEIGHT = document.createElement('div');
        DIV_ELE_GROUPING_BUTTONS.append(BUTTON_ELE_1, BUTTON_ELE_2, BUTTON_ELE_3);
        img.addEventListener('load', () => {
            var img_width = img.width;
            var img_height = img.height;

            if (img_width >= img_height) {
                DIV_ELE_GROUPING_BUTTONS.setAttribute("class", "d-inline-block col-12");

                IMG_ELE.setAttribute("class", "w-100");
                DIV_CARD.append(DIV_ELE_4, DIV_ELE_GROUPING_BUTTONS);
                DIV_ELE_GROUPING_BUTTONS.setAttribute("style", "padding-top: 10%");
                BUTTON_ELE_1.innerText = "Download";
                BUTTON_ELE_2.innerText = "Update";
                BUTTON_ELE_3.innerText = "Delete";

                BUTTON_ELE_1.setAttribute("style", "width: 100px");
                BUTTON_ELE_2.setAttribute("style", "width: 100px");
                BUTTON_ELE_3.setAttribute("style", "width: 100px");

            } else {
                DIV_ELE_GROUPING_BUTTONS.setAttribute("class", "d-flex flex-column col-12");
                A_CARD_BACKGROUND.setAttribute("style", "width:10%");

                IMG_ELE.setAttribute("style", "object-fit: cover; display: flex; width: 70%");
                DIV_GREAT_HEIGHT.append(DIV_ELE_4, DIV_ELE_GROUPING_BUTTONS);
                DIV_GREAT_HEIGHT.setAttribute("style", "display: flex; flex-direction: column; overflow: hidden; padding-top: 20%");
                DIV_IMG_FRAME.append(DIV_GREAT_HEIGHT);
                DIV_IMG_FRAME.setAttribute("style", "display: flex");
                DIV_ELE_GROUPING_BUTTONS.setAttribute("style", "padding-top: 80%");

                const IMG_DOWNLOAD_ELE = document.createElement('img')
                IMG_DOWNLOAD_ELE.setAttribute("src", "logo/download_icon.png")
                IMG_DOWNLOAD_ELE.setAttribute("width", "24")
                IMG_DOWNLOAD_ELE.setAttribute("height", "24")
                BUTTON_ELE_1.append(IMG_DOWNLOAD_ELE)

                const IMG_UPDATE_ELE = document.createElement('img')
                IMG_UPDATE_ELE.setAttribute("src", "logo/update_icon.png")
                IMG_UPDATE_ELE.setAttribute("width", "24")
                IMG_UPDATE_ELE.setAttribute("height", "24")
                BUTTON_ELE_2.append(IMG_UPDATE_ELE)

                const IMG_DELETE_ELE = document.createElement('img')
                IMG_DELETE_ELE.setAttribute("src", "logo/delete_icon.png")
                IMG_DELETE_ELE.setAttribute("width", "24")
                IMG_DELETE_ELE.setAttribute("height", "24")
                BUTTON_ELE_3.append(IMG_DELETE_ELE)

                BUTTON_ELE_1.setAttribute("style", "width: 50px");
                BUTTON_ELE_2.setAttribute("style", "width: 50px");
                BUTTON_ELE_3.setAttribute("style", "width: 50px");

            }
        });

        document.querySelector("#scroll-row").append(DIV_CARD);
        isFetching = false; // callback이 끝났으니 isFetching 리셋
    });
    lastId++;
};

const getList = () => {
    isFetching = true; // 아직 callback이 끝나지 않았어요!

    fetch(URL_PAGING + "?lastPageId=" + lastId.toString() + "&pageSize=" + defaultPaginationSize.toString())
        .then(response => response.json())
        .then(drawList)
        .catch((e) => {
            console.log(e);
        });
};

function downloadImg(post_id) {

    let filename = ' ';
    fetch("/images/" + post_id + "/download")
        .then(response => {
            const contentDispositionHeader = response.headers.get('Content-Disposition');
            const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
            const matches = filenameRegex.exec(contentDispositionHeader);

            if (matches != null && matches[1]) {
                filename = matches[1].replace(/['"]/g, '');
            }

            return response.blob();
        })
        .then(function (response) {
            let link = document.createElement('a');
            link.style.display = 'none';
            document.body.appendChild(link);
            link.href = window.URL.createObjectURL(response);
            link.download = filename;
            link.click();
        }).catch((e) => {
    });
}

window.addEventListener("scroll", function () {

    const isAtBottom = (window.innerHeight + window.scrollY) >= document.body.offsetHeight - 500;

    if (isAtBottom && !isFetching) { // isFetching이 false일 때 조건 추가
        queryGetList();
    }
});

function queryGetList() {
    if (isQuerying) {
        return;
    }

    isQuerying = true;

    getList();

    setTimeout(() => {
        isQuerying = false; // 딜레이가 종료되면 쿼리 실행 상태를 false로 설정합니다.
    }, 300);
}

queryGetList()
