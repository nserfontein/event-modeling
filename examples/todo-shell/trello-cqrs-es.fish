#set up the dir structure if it doesn't exist
set t ~/.trello; mkdir -p $t
function if_exist # we don't want to litter our code with ifs if a read model or events are not there yet. Return /dev/nul for empty
   if test -e $t/$argv[1]; echo $t/$argv[1]; else; echo /dev/null; end
end
function list # provide lists from our tab delimited entries with filtering and line item numbers to make choices
   set list $argv[1]; set column_to_show $argv[2]; set filter $argv[3]; set count 1
   cat (if_exist $list) | grep -e "$filter\$" | while read line
       set -l line_items (string split \t $line)
       echo $count\) $line_items[$column_to_show]
       set count (math $count + 1)
   end
end
function lookup_id # grab ids by looking up values in event stream or read models
   set list $argv[1]; set filter $argv[2]; set list_num $argv[3]
   set line_items (grep "$filter\$" $t/$list | sed $list_num"q;d" | string split \t)[1]
   echo $line_items[1]
end
function show_board # our example of a screen that uses a read model
   paste ( begin; echo Todo:; list rm_items 2 todo; end | psub) ( begin; echo Done:; list rm_items 2 done; end | psub) | sed 's/\t/ \t/' | column -s \t -t | while read line
       if not set -q not_first_anymore
           set_color cyan; echo $line; set_color normal
       else
           echo $line
       end
       set not_first_anymore 1
   end
   set -e not_first_anymore
end
while true
   clear
   set_color green; echo Trello App\n; set_color normal
   show_board
   echo; read -P "(C)reate (F)inish (Q)uit: " choice
   set event
   switch $choice # our source of issuing commands
       case Q or q; break;
       case C or c; read -P 'Task name: ' name; set -g event (date +%s)\t"TaskCreated"\t"$name"
       case F or f; read -P 'Finish item: ' item;
           set -g event (date +%s)\t"TaskFinished"\t(lookup_id rm_items todo $item);
   end
   if test -n $event
       echo $event >> $t/events # append our event stream
       set event_type (echo $event | string split \t)[2]; set event_id (echo $event | string split \t)[1];
       switch $event_type # our "pub sub"
           case TaskCreated; echo (string replace TaskCreated\t '' $event)\ttodo >> $t/rm_items; # we only have one read model in this one
           case TaskFinished; set items (string split \t $event); set id $items[3]; echo id: $id; sed -i "s/\($id.*\)todo/\\1done/" $t/rm_items;
       end

   end
end
