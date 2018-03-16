/*--Rooms and Rates

--Some overlap will occur when calculating popularity as people checkout and checkin on the same date, leading to a double count
SELECT RoomCode, RoomName, Beds, bedType, maxOcc, basePrice, decor
, ROUND(SUM(DATEDIFF(Checkout, CheckIn))/180, 2) AS Room_Popularity_Score 
, (SELECT R1.Checkout FROM lab6_reservations R1
WHERE Room = RoomCode AND R1.Checkout >= Now()
AND R1.Checkout NOT IN (
SELECT CheckIn FROM lab6_reservations R2
WHERE Room = RoomCode AND R1.Checkout >= Now())
LIMIT 1) AS Next_Avail_Date
, (SELECT DATEDIFF(Checkout, CheckIn) FROM lab6_reservations
WHERE Checkout <= NOW() AND Room = RoomCode
ORDER BY Checkout DESC
LIMIT 1) AS Last_Duration
, (SELECT Checkout FROM lab6_reservations
WHERE Checkout <= NOW() AND Room = RoomCode
ORDER BY Checkout DESC
LIMIT 1) AS Last_Checkout
FROM lab6_reservations AS Resv JOIN lab6_rooms AS R ON Resv.Room = R.RoomCode
WHERE (CheckIn >= DATE_SUB(NOW(), INTERVAL 180 day) OR CheckOut >= DATE_SUB(NOW(), INTERVAL 180 day))
AND (CheckIn < NOW() OR CheckOut < NOW())
GROUP BY Room
ORDER BY Room_Popularity_Score DESC;


--Reservations
*/
-- Detailed Reservation Information
SELECT Room, MONTH(CheckOut), COUNT(*) FROM Reservations
WHERE YEAR(CheckOut) = YEAR(DATE("2010-09-10"))
GROUP BY Room, MONTH(CheckOut);

-- Revenue