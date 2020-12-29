# Architecture

## Database model

<div style="width:600px;">

![alt text](database_diagram.png "Navigation between activities")
</div>

## Activities

<div style="width:600px;">

![alt text](activities.jpg "Navigation between activities")
</div>

## Timer
The following diagram shows the sequence when the timer is running. This complex sequence is necessary because Android forbids processes running in the background without a extra permission.

By using the Android alarm, there is no need to run the timer in the background.

<div style="width:1000px;">


![uncached image](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/pase13voxi/TeaMemory/master/documentation/architecture/sequence_diagram_timer.txt "Timer sequence")
</div>
