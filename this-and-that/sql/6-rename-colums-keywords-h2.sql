alter table season add ended date default null;
update season set ended=end;
alter table season drop column end;

alter table quarterly_season add ended date default null;
update quarterly_season set ended=end;
alter table quarterly_season drop column end;

alter table toc_config add yearr int not null;
update toc_config set yearr=year;
alter table toc_config drop column year;
