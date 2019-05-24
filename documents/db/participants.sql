CREATE TABLE sqills.participants (
  userID integer,
  bookingID integer,
  FOREIGN KEY (userID) REFERENCES sqills.users(userID),
  FOREIGN KEY (bookingID) REFERENCES sqills.booking(bookingID)
);