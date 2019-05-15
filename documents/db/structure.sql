CREATE TABLE sqills.Booking (
    bookingID INTEGER,
    startTime TIMESTAMP,
    endTime TIMESTAMP,
    roomID INTEGER,
    PRIMARY KEY (bookingID),
    FOREIGN KEY (roomID) REFERENCES sqills.Room(roomID)
);

CREATE TABLE sqills.Room (
    roomID INTEGER,
    PRIMARY KEY(roomID)
);