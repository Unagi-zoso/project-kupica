let count_of_img = 0;
const PODIUM_IMG_URL = "/images/latest/5";

const draw_podium_list = (DATA) => {

    DATA.value.forEach((item) => {

        const { cached_image_url } = item;

        const DIV_CARD = document.createElement('div');
        if (count_of_img === 0) DIV_CARD.setAttribute("class", "carousel-item active");
        else DIV_CARD.setAttribute("class", "carousel-item");

        const IMG_ELE = document.createElement('img');
        IMG_ELE.setAttribute("class", "d-block w-100 -b");
        IMG_ELE.setAttribute("src", cached_image_url);

        DIV_CARD.append(IMG_ELE);

        document.querySelector(".carousel-main").append(DIV_CARD);
        count_of_img++;
    });
};

const get_podium_List = () => {
    fetch(PODIUM_IMG_URL)
        .then(response => response.json())
        .then(draw_podium_list)
        .catch((e) => {
            console.log(e);
        });
};

get_podium_List()
