CREATE TABLE sqills.participants (
  userID integer not null,
  bookingID integer not null,
  FOREIGN KEY (userID) REFERENCES sqills.users(userID),
  FOREIGN KEY (bookingID) REFERENCES sqills.booking(bookingID)
);