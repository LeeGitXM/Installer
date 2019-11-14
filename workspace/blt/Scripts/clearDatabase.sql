-- drop all user defined stored procedures
Declare @procName varchar(500) 
Declare cur Cursor For Select [name] From sys.objects where type = 'p' 
Open cur 
Fetch Next From cur Into @procName 
While @@fetch_status = 0 
Begin 
  Exec('drop procedure ' + @procName) 
  Fetch Next From cur Into @procName 
End
Close cur 
 Deallocate cur 


-- drop all user defined triggers
Declare @trgName varchar(500) 
Declare cur Cursor For Select [name] From sys.objects where type = 'tr' 
Open cur 
Fetch Next From cur Into @trgName 
While @@fetch_status = 0 
Begin 
  Exec('drop trigger ' + @trgName) 
  Fetch Next From cur Into @trgName 
End
Close cur 
Deallocate cur 

-- drop all user defined views
Declare @viewName varchar(500) 
Declare cur Cursor For Select [name] From sys.objects where type = 'v' 
Open cur 
Fetch Next From cur Into @viewName 
While @@fetch_status = 0 
Begin 
  Exec('drop view ' + @viewName) 
  Fetch Next From cur Into @viewName 
End
Close cur 
Deallocate cur 

-- drop all user defined tables

DECLARE @Sql NVARCHAR(500) DECLARE @Cursor CURSOR

SET @Cursor = CURSOR FAST_FORWARD FOR
SELECT DISTINCT sql = 'ALTER TABLE [' + tc2.TABLE_NAME + '] DROP [' + rc1.CONSTRAINT_NAME + ']'
FROM INFORMATION_SCHEMA.REFERENTIAL_CONSTRAINTS rc1
LEFT JOIN INFORMATION_SCHEMA.TABLE_CONSTRAINTS tc2 ON tc2.CONSTRAINT_NAME =rc1.CONSTRAINT_NAME

OPEN @Cursor FETCH NEXT FROM @Cursor INTO @Sql

WHILE (@@FETCH_STATUS = 0)
BEGIN
Exec SP_EXECUTESQL @Sql
FETCH NEXT FROM @Cursor INTO @Sql
END

CLOSE @Cursor DEALLOCATE @Cursor
GO

EXEC sp_MSForEachTable 'DROP TABLE ?'
GO

Declare @functionName varchar(500) 
Declare cur Cursor For Select [name] From sys.objects where type = 'tf' 
Open cur 
Fetch Next From cur Into @functionName 
While @@fetch_status = 0 
Begin 
  Exec('drop function ' + @functionName) 
  Fetch Next From cur Into @functionName 
End
Close cur 
Deallocate cur 
GO
