const methods = require('../specific-room');

test('date 10-10-2018 is from past', () => {
    expect(methods.checkIsFromPastDays("10-10-2018")).toBe(true);
});

let d = new Date().toDateString();
test('todays date is todays date and one from the past is not', () => {
    expect(methods.checkIfBookingToday(d)).toBe(true);
    expect(methods.checkIfBookingToday("10-10-2018")).toBe(false);
});

test('todays date is todays date and one from the past is not', () => {
    document.body.innerHTML =
        "<div class='modal'></div>";

    expect(methods.checkIfBookingToday(d)).toBe(true);
    expect(methods.checkIfBookingToday("10-10-2018")).toBe(false);
});

