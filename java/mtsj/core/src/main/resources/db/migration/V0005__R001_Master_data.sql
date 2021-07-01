INSERT INTO UserRole(id, modificationCounter, name, active)
VALUES
	(0, 1, 'Customer', true),
	(1, 1, 'Waiter', true),
	(2, 1, 'Manager', true),
	(3, 1, 'Admin', true);

INSERT INTO User(id, modificationCounter, username, password, twoFactorStatus, email, idRole)
VALUES
	(0, 1, 'admin', '{bcrypt}$2y$10$jC2ewvUv1ROPLpqwhu/gTOAnsDkFEM4J7Vs4nfi5SYyyQLra6XaXa', false, 'admin@mail.com', 3),
	(1, 1, 'manager', '{bcrypt}$2a$10$IsTlZemkiPKE2gjtnSMlJOX5.uitNHXNRpLYyvyxNbHEhjpY.XdTq', false, 'manager@mail.com', 2),
	(2, 1, 'waiter', '{bcrypt}$2a$10$1CAKyUHbX6RJqT5cUP6/aOMTIlYYvGIO/a3Dt/erbYKKgmbgJMGsG', false, 'waiter@mail.com', 1),
	(3, 1, 'user0', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'user0@mail.com', 0),
	(4, 1, 'HansMüller33', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC' , false, 'Hans.Müller@mail.com', 0),
	(5, 1, 'Lena123', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'Lena.Weber@mail.com', 0),
	(6, 1, 'SofieS', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'sweetgirl.sofie@mail.com', 1),
	(7, 1, 'timboy', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'timgoodboy1998@gmail.com', 0),
	(8, 1, 'MeyerDieter', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'Dieter.Meyer@mail.com', 0),
	(9, 1, 'JonasSchmidt', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'Jonas.Schmidt@mail.com', 0),
	(10, 1, 'Stefano', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'Stefan543@mail.com', 0),
	(11, 1, 'JuliaW', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'Julia.Wagner@mail.com', 0),
	(12, 1, 'LukasHof', '{bcrypt}$2y$10$gB46gK0lqpTfuYPlJfEyTOKNrBGZ2Tsu6zatwp4akcCTxFsxjNvXC', false, 'LukasHof@mail.com', 0);

INSERT INTO "Table" (id, modificationCounter, seatsNumber)
VALUES
  (1, 1, 4),
  (2, 1, 4),
  (3, 1, 4),
  (4, 1, 4),
  (5, 1, 4),
  (6, 1, 4),
  (7, 1, 4),
  (8, 1, 4),
	(9, 1, 4),
	(10, 1, 4),
	(11, 1, 6),
	(12, 1, 6),
	(13, 1, 6),
	(14, 1, 6),
	(15, 1, 6),
	(16, 1, 6),
	(17, 1, 8),
	(18, 1, 8),
  (19, 1, 8),
  (20, 1, 8);

INSERT INTO Booking(id, modificationCounter, idUser, name, bookingToken, comment, email, bookingDate, expirationDate, creationDate, canceled, bookingType, idTable, idOrder, assistants)
VALUES
	-- Common Booking with already created orders
	(0, 1, 3, 'user0', 'CB_20170509_123502550Z', null, 'user0@mail.com', DATEADD('DAY', 1, CURRENT_TIMESTAMP), DATEADD('DAY', 1, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 1, null, 3),
	(1, 1, 4, 'HansMüller33', 'CB_20170509_123502551Z', null, 'Hans.Müller@mail.com', DATEADD('DAY', 2, CURRENT_TIMESTAMP), DATEADD('DAY', 2, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 3, null, 2),
	(2, 1, 5, 'Lena123', 'CB_20170509_123502552Z', null, 'Lena.Weber@mail.com', DATEADD('DAY', 3, CURRENT_TIMESTAMP), DATEADD('DAY', 3, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 11, null, 5),
	(3, 1, 10, 'Stefano', 'CB_20170509_123502553Z', null, 'Stefan543@mail.com', DATEADD('DAY', 4, CURRENT_TIMESTAMP), DATEADD('DAY', 4, DATEADD('HOUR', -1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 13, null, 5),
	(4, 1, 4, 'HansMüller33', 'CB_20170509_123502554Z', null, 'Hans.Müller@mail.com', CURRENT_TIMESTAMP, DATEADD('HOUR', -1, CURRENT_TIMESTAMP), CURRENT_TIMESTAMP, false, 0, 4, null, 3),
  (5, 1, 5, 'Lena123', 'CB_20170509_123502555Z', null, 'Lena.Weber@mail.com', DATEADD('HOUR', 1, CURRENT_TIMESTAMP), DATEADD('HOUR', -1, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 0, 4, null, 2),

  -- Common Booking without orders:
	(6, 1, 8, 'MeyerDieter', 'CB_20170509_123502562Z', null, 'Dieter.Meyer@mail.com', DATEADD('HOUR', 1, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP)), DATEADD('HOUR', -1, DATEADD('HOUR', 1, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP))), CURRENT_TIMESTAMP, false, 0, 2, null, 4),
	(7, 1, 9, 'JonasSchmidt', 'CB_20170509_123502556Z', null, 'Jonas.Schmidt@mail.com', DATEADD('HOUR', 1, DATEADD('MINUTE', 20, CURRENT_TIMESTAMP)), DATEADD('HOUR', -1, DATEADD('HOUR', 1, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP))), CURRENT_TIMESTAMP, false, 0, 6, null, 3),
	(8, 1, 5, 'Lena123', 'CB_20170509_123502557Z', null, 'Lena.Weber@mail.com', DATEADD('HOUR', 1, DATEADD('MINUTE', 20, CURRENT_TIMESTAMP)), DATEADD('HOUR', -1, DATEADD('HOUR', 1, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP))), CURRENT_TIMESTAMP, false, 0, 15, null, 5),
	(9, 1, 12, 'LukasHof', 'CB_20170509_123502558Z', null, 'LukasHof@mail.com', DATEADD('HOUR', 1, DATEADD('MINUTE', 30, CURRENT_TIMESTAMP)), DATEADD('HOUR', -1, DATEADD('HOUR', 1, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP))), CURRENT_TIMESTAMP, false, 0, 8, null, 4),
	(10, 1, 11, 'JuliaW', 'CB_20170509_123502559Z', null, 'Julia.Wagner@mail.com', DATEADD('HOUR', 1, DATEADD('MINUTE', 40, CURRENT_TIMESTAMP)), DATEADD('HOUR', -1, DATEADD('HOUR', 1, DATEADD('MINUTE', 10, CURRENT_TIMESTAMP))), CURRENT_TIMESTAMP, false, 0, 9, null, 2),

  -- Booking for invited guests and created order
  (20, 1, 9, 'JonasSchmidt', 'CB_20170509_123502560Z', null, 'Jonas.Schmidt@mail.com', DATEADD('HOUR', 1, CURRENT_TIMESTAMP), DATEADD('HOUR', -1, DATEADD('HOUR', 1, CURRENT_TIMESTAMP)), CURRENT_TIMESTAMP, false, 1, 8, null, 5),

  -- Booking 1 hour and 58 minutes before now.
  (30, 1, 8, 'MeyerDieter', 'CB_20170509_123502561Z', null, 'Dieter.Meyer@mail.com', DATEADD('MINUTE', 2, DATEADD('HOUR', -2, CURRENT_TIMESTAMP)), DATEADD('HOUR', -1, DATEADD('MINUTE', 3, DATEADD('HOUR', -2, CURRENT_TIMESTAMP))), CURRENT_TIMESTAMP, false, 0, 1, null, 3);

-- Invited guests for above booking with id 20
INSERT INTO InvitedGuest(id, modificationCounter, idBooking, guestToken, email, accepted, modificationDate)
VALUES
	(0, 1, 20, 'GB_20170510_52350266501Z', 'guest1@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP)),
	(1, 1, 20, 'GB_20170510_62350266501Z', 'guest2@mail.com', false, DATEADD('DAY', 5, CURRENT_TIMESTAMP)),
	(2, 1, 20, 'GB_20170510_72350266501Z', 'guest3@mail.com', null, DATEADD('DAY', 5, CURRENT_TIMESTAMP)),
	(3, 1, 20, 'GB_20170510_82350266501Z', 'guest4@mail.com', true, DATEADD('DAY', 5, CURRENT_TIMESTAMP));