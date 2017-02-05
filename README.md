# Sakoon Ki Namaz

Auto Silent your phone at prayers time.

###Front End Flow:
- On application launch: Five prayers are added by default
- Set the time slot for each prayer
- You can add more slots too ('+' button at top right)
- You can also delete a slot (click on list item)
- App will auto silent you phone in added time slots
- In silent mode slots it will show a reason notification

### Backend Flow
- Time slots are added in android database 
- onStop of app will activate PrayerTimeService
- PrayerTimeService sorts the database time slots w.r.t to start time
- Get the device time and set the alarm of next coming silent mode slot
- Two alarm are set one for start time and one for end time using alarm manager
- Alarm manager actives MatchTimeReceiver(Broadcast Receiver) at start time
- Check if start time alarm then get the current app audio profile and put phone on silent
- Notification Service is started at start time with silent slot name
- When it's end time alarm phone is reverted bach to original profile and Notification service stops
- At end time alarm PrayerTimeService starts again

#####For Any question: `4a5e3b@gmail.com`
