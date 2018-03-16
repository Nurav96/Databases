--Rooms and Rates
SELECT *, poop FROM lab6_rooms
WHERE RoomCode IN (
SELECT Room as poop FROM lab6_reservations
WHERE Room = "HBB" 
GROUP BY Room

--Some overlap will occur when calculating popularity as people checkout and checkin on the same date, leading to a double count
SELECT Room
, ROUND(SUM(Checkout - CheckIn)/180, 2) AS Room_Popularity_Score 
FROM lab6_reservations
WHERE (CheckIn >= DATE_SUB(NOW(), INTERVAL 180 day) OR CheckOut >= DATE_SUB(NOW(), INTERVAL 180 day))
AND (CheckIn < NOW() OR CheckOut < NOW())
GROUP BY Room
ORDER BY Room_Popularity_Score DESC;

SELECT *
FROM lab6_reservations R1 JOIN lab6_reservations R2 ON R1.Room = R2.Room AND R1.CheckIn != R2.Checkout
WHERE (R1.CheckIn >= NOW() OR R1.CheckOut >= NOW())
AND (R2.CheckIn >= NOW() OR R2.CheckOut >= NOW());

SELECT * FROM lab6_reservations R1
WHERE Room = "IBS" AND DATE_ADD(R1.Checkout, INTERVAL 1 day) NOT IN (
SELECT CheckIn FROM lab6_reservations R2);

SELECT DATE_ADD(R1.Checkout, INTERVAL 1 day) AS Next_Avail_Date FROM lab6_reservations R1
WHERE Room = "IBS" AND R1.Checkout >= Now()
AND (R1.Checkout NOT IN (
SELECT CheckIn FROM lab6_reservations R2)
OR DATE_ADD(R1.Checkout, INTERVAL 1 day) NOT IN (
SELECT CheckIn FROM lab6_reservations R3)) LIMIT 1;
--Reservations

--Detailed Reservation Information

--Revenue