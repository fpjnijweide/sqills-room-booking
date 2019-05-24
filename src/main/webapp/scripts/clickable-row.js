//Todo: fix
$(document).ready(() =>  {
    let elems = document.getElementsByClassName("clickable-row");
    console.log(elems);
    for (let i = 0; i < elems.length; i++) {
        let elem = elems[i];
        elem.addEventListener("onclick", () => {
            console.log('hi');
        }, false);
    }
});