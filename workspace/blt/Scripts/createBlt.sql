/****** Object:  Table [dbo].[DtFinalDiagnosisLog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DtFinalDiagnosisLog](
	[LogId] [int] IDENTITY(1,1) NOT NULL,
	[Timestamp] [datetime] NOT NULL,
	[FinalDiagnosisId] [int] NOT NULL,
	[State] [bit] NULL,
	[Active] [bit] NULL,
 CONSTRAINT [PK_DtFinalDiagnosisLog] PRIMARY KEY CLUSTERED 
(
	[LogId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DtDiagnosisEntry]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtDiagnosisEntry](
	[DiagnosisEntryId] [int] IDENTITY(1,1) NOT NULL,
	[FinalDiagnosisId] [int] NOT NULL,
	[Status] [varchar](50) NOT NULL,
	[Timestamp] [datetime] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[TextRecommendation] [varchar](1000) NOT NULL,
	[RecommendationStatus] [varchar](50) NOT NULL,
	[Multiplier] [float] NOT NULL,
	[RecommendationErrorText] [varchar](1000) NULL,
 CONSTRAINT [PK_DtDiagnosisEntry] PRIMARY KEY CLUSTERED 
(
	[DiagnosisEntryId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[alarm_events]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[alarm_events](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[eventid] [varchar](255) NULL,
	[source] [varchar](255) NULL,
	[displaypath] [varchar](255) NULL,
	[priority] [int] NULL,
	[eventtype] [int] NULL,
	[eventflags] [int] NULL,
	[eventtime] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[alarm_event_data]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[alarm_event_data](
	[id] [int] NULL,
	[propname] [varchar](255) NULL,
	[dtype] [int] NULL,
	[intvalue] [bigint] NULL,
	[floatvalue] [float] NULL,
	[strvalue] [varchar](255) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE NONCLUSTERED INDEX [alarm_event_dataidndx] ON [dbo].[alarm_event_data] 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DtApplicationManageQueue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtApplicationManageQueue](
	[ApplicationName] [varchar](250) NOT NULL,
	[Provider] [varchar](50) NOT NULL,
	[Timestamp] [datetime] NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[BtReactor]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BtReactor](
	[ReactorId] [int] IDENTITY(1,1) NOT NULL,
	[ReactorName] [varchar](50) NOT NULL,
	[TagName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_BtReactor] PRIMARY KEY CLUSTERED 
(
	[ReactorId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[CustomStatsTable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CustomStatsTable](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Parameter] [varchar](50) NULL,
	[Mean] [float] NULL,
	[Median] [float] NULL,
	[StandardDeviation] [float] NULL,
 CONSTRAINT [PK_CustomStatsTable] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[CustomRateTable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[CustomRateTable](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Grade] [varchar](50) NULL,
	[Rate] [int] NULL,
	[Cost] [int] NULL,
 CONSTRAINT [PK_CustomRateTable] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[HBLabReport]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[HBLabReport](
	[Id] [bigint] IDENTITY(1,1) NOT NULL,
	[LabValue] [float] NULL,
	[USpec] [float] NULL,
	[UCtl] [float] NULL,
	[Target] [float] NULL,
	[LCtl] [float] NULL,
	[LSpec] [float] NULL,
	[ReceiveTime] [datetime] NULL,
	[ReceiveText] [text] NULL,
	[SampleTime] [datetime] NULL,
	[Status] [varchar](50) NULL,
	[EventDescription] [varchar](50) NULL,
	[BoxNoteText] [varchar](300) NULL,
	[OCConfirm] [varchar](50) NULL,
	[DeleteFlag] [bit] NULL,
	[ReasonForRejection] [varchar](300) NULL,
	[UIRStatus] [varchar](50) NULL,
	[ActionTaken] [varchar](300) NULL,
	[DescriptionText] [varchar](300) NULL,
	[CurrentGrade] [varchar](50) NULL,
	[ProblemText] [varchar](300) NULL,
	[UIRId] [int] NULL,
	[BackgroundColor] [varchar](20) NULL,
	[ForegroundColor] [varchar](20) NULL,
 CONSTRAINT [PK_HBLabReport] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LookupType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LookupType](
	[LookupTypeCode] [varchar](15) NOT NULL,
	[LookupTypeName] [varchar](50) NOT NULL,
	[LookupTypeDescription] [varchar](500) NULL,
 CONSTRAINT [PK_LookupType] PRIMARY KEY CLUSTERED 
(
	[LookupTypeCode] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtHDAInterface]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtHDAInterface](
	[InterfaceId] [int] IDENTITY(1000,1) NOT NULL,
	[InterfaceName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_LtHDAInterface] PRIMARY KEY CLUSTERED 
(
	[InterfaceId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtOPCInterface]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtOPCInterface](
	[InterfaceId] [int] IDENTITY(1000,1) NOT NULL,
	[InterfaceName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_LtOPCInterface] PRIMARY KEY CLUSTERED 
(
	[InterfaceId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_LtOPCInterface] ON [dbo].[LtOPCInterface] 
(
	[InterfaceName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtDownloadDetail]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtDownloadDetail](
	[DetailId] [int] IDENTITY(1,1) NOT NULL,
	[MasterId] [int] NOT NULL,
	[Timestamp] [datetime] NOT NULL,
	[Tag] [varchar](max) NOT NULL,
	[OutputValue] [nvarchar](50) NULL,
	[Success] [bit] NOT NULL,
	[StoreValue] [nvarchar](50) NULL,
	[CompareValue] [nvarchar](50) NULL,
	[RecommendedValue] [nvarchar](50) NULL,
	[Reason] [varchar](2000) NULL,
	[Error] [varchar](2000) NULL,
 CONSTRAINT [PK_DownloadDetail] PRIMARY KEY CLUSTERED 
(
	[DetailId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtAllowedFlyingSwitch]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtAllowedFlyingSwitch](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[CurrentGrade] [varchar](50) NOT NULL,
	[NextGrade] [varchar](50) NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtAdhocCatalog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtAdhocCatalog](
	[TableName] [varchar](500) NOT NULL,
 CONSTRAINT [PK_RtAdhocCatalog] PRIMARY KEY CLUSTERED 
(
	[TableName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RoleTranslation]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RoleTranslation](
	[IgnitionRole] [varchar](50) NOT NULL,
	[WindowsRole] [varchar](50) NOT NULL,
 CONSTRAINT [PK_RoleTranslation] PRIMARY KEY CLUSTERED 
(
	[IgnitionRole] ASC,
	[WindowsRole] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[QueueMessageStatus]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[QueueMessageStatus](
	[StatusId] [int] IDENTITY(1,1) NOT NULL,
	[Severity] [real] NOT NULL,
	[MessageStatus] [nvarchar](15) NOT NULL,
	[Color] [nvarchar](15) NOT NULL,
 CONSTRAINT [PK_QueueMessageStatus] PRIMARY KEY CLUSTERED 
(
	[StatusId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[QueueMaster]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[QueueMaster](
	[QueueId] [int] IDENTITY(1,1) NOT NULL,
	[QueueKey] [nvarchar](50) NOT NULL,
	[Title] [nvarchar](100) NOT NULL,
	[CheckpointTimestamp] [datetime] NULL,
	[AutoViewSeverityThreshold] [real] NOT NULL,
	[Position] [varchar](50) NOT NULL,
	[AutoViewAdmin] [bit] NOT NULL,
	[AutoViewAE] [bit] NOT NULL,
	[AutoViewOperator] [bit] NOT NULL,
 CONSTRAINT [PK_QueueMaster] PRIMARY KEY CLUSTERED 
(
	[QueueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [IX_QueueMaster] ON [dbo].[QueueMaster] 
(
	[QueueKey] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcChart]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcChart](
	[ChartId] [int] IDENTITY(1,1) NOT NULL,
	[ChartPath] [varchar](800) NULL,
	[ChartResourceId] [int] NULL,
	[CreateTime] [datetime] NULL,
	[IsProduction] [bit] NOT NULL,
 CONSTRAINT [PK_SfcCharts] PRIMARY KEY CLUSTERED 
(
	[ChartId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_SfcChart] ON [dbo].[SfcChart] 
(
	[ChartPath] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtWatchdog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RtWatchdog](
	[Observation] [int] NOT NULL,
	[Timestamp] [datetime] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[Observation] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtValueType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtValueType](
	[ValueTypeId] [int] IDENTITY(1,1) NOT NULL,
	[ValueType] [varchar](25) NOT NULL,
 CONSTRAINT [PK_RtValueType] PRIMARY KEY CLUSTERED 
(
	[ValueTypeId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [IX_RtValueType] ON [dbo].[RtValueType] 
(
	[ValueType] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcDialogMessage]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcDialogMessage](
	[windowId] [varchar](900) NOT NULL,
	[message] [varchar](900) NOT NULL,
	[ackRequired] [bit] NOT NULL,
	[acknowledged] [bit] NULL,
 CONSTRAINT [PK_SfcDialogMsg] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataValue](
	[ValueId] [int] IDENTITY(1,1) NOT NULL,
	[FloatValue] [float] NULL,
	[IntegerValue] [int] NULL,
	[StringValue] [varchar](1000) NULL,
	[BooleanValue] [bit] NULL,
 CONSTRAINT [PK_SfcRecipeDataValue] PRIMARY KEY CLUSTERED 
(
	[ValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataType](
	[RecipeDataTypeId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeDataType] [varchar](50) NOT NULL,
	[JavaClassName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_SfcRecipeDataTypes] PRIMARY KEY CLUSTERED 
(
	[RecipeDataTypeId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataOutputType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataOutputType](
	[OutputTypeId] [int] IDENTITY(1,1) NOT NULL,
	[OutputType] [varchar](50) NOT NULL,
 CONSTRAINT [PK_SfcRecipeDataOutputType] PRIMARY KEY CLUSTERED 
(
	[OutputTypeId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcNames]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcNames](
	[SfcName] [varchar](500) NOT NULL,
 CONSTRAINT [PK_SfcNames] PRIMARY KEY CLUSTERED 
(
	[SfcName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataKeyMaster]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataKeyMaster](
	[KeyId] [int] IDENTITY(1,1) NOT NULL,
	[KeyName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_SfcRecipeDataKeyMaster] PRIMARY KEY CLUSTERED 
(
	[KeyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkAssociationType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkAssociationType](
	[AssociationTypeId] [int] IDENTITY(1,1) NOT NULL,
	[AssociationType] [varchar](100) NOT NULL,
 CONSTRAINT [PK_TkAssociationType] PRIMARY KEY CLUSTERED 
(
	[AssociationTypeId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkAssociation]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkAssociation](
	[AssociationId] [int] IDENTITY(1,1) NOT NULL,
	[Source] [varchar](250) NOT NULL,
	[Sink] [varchar](250) NOT NULL,
	[AssociationTypeId] [int] NOT NULL,
 CONSTRAINT [PK_TkAssociation] PRIMARY KEY CLUSTERED 
(
	[AssociationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcSaveData]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcSaveData](
	[windowId] [varchar](900) NOT NULL,
	[text] [varchar](max) NOT NULL,
	[computer] [varchar](900) NOT NULL,
	[printText] [bit] NOT NULL,
	[viewText] [bit] NOT NULL,
	[showPrintDialog] [bit] NOT NULL,
	[filePath] [varchar](900) NULL,
 CONSTRAINT [PK_SfcSaveData] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRunLog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRunLog](
	[RunId] [int] IDENTITY(1000,1) NOT NULL,
	[ChartPath] [varchar](250) NOT NULL,
	[StepName] [varchar](50) NOT NULL,
	[StepType] [varchar](50) NOT NULL,
	[StartTime] [datetime] NOT NULL,
	[EndTime] [datetime] NULL,
	[Status] [varchar](20) NULL,
	[Notes] [varchar](2000) NULL,
 CONSTRAINT [PK_SfcRunLog] PRIMARY KEY CLUSTERED 
(
	[RunId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkLogbook]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkLogbook](
	[LogbookId] [int] IDENTITY(1,1) NOT NULL,
	[LogbookName] [varchar](20) NOT NULL,
 CONSTRAINT [PK_TkLogbook] PRIMARY KEY CLUSTERED 
(
	[LogbookId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UK_TkLogbook] UNIQUE NONCLUSTERED 
(
	[LogbookName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkMenuBar]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkMenuBar](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[Application] [varchar](50) NOT NULL,
	[Menu] [varchar](50) NOT NULL,
	[SubMenu] [varchar](50) NOT NULL,
	[Enabled] [bit] NOT NULL,
 CONSTRAINT [PK_TkMenuBar] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkMessageRequest]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkMessageRequest](
	[RequestId] [int] IDENTITY(1,1) NOT NULL,
	[RequestType] [varchar](50) NOT NULL,
	[RequestTime] [datetime] NOT NULL,
 CONSTRAINT [PK_TkMessageRequest] PRIMARY KEY CLUSTERED 
(
	[RequestId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcValueType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcValueType](
	[ValueTypeId] [int] IDENTITY(1,1) NOT NULL,
	[ValueType] [varchar](50) NOT NULL,
 CONSTRAINT [PK_SfcDataType] PRIMARY KEY CLUSTERED 
(
	[ValueTypeId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataStash]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataStash](
	[StashId] [int] IDENTITY(1,1) NOT NULL,
	[RxConfiguration] [varchar](50) NOT NULL,
	[RecipeDataKey] [varchar](50) NOT NULL,
	[RecipeDataAttribute] [varchar](50) NOT NULL,
	[RecipeDataValue] [float] NOT NULL,
 CONSTRAINT [PK_EmRecipeDataStash] PRIMARY KEY CLUSTERED 
(
	[StashId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_EmRecipeDataStash] ON [dbo].[SfcRecipeDataStash] 
(
	[RxConfiguration] ASC,
	[RecipeDataKey] ASC,
	[RecipeDataAttribute] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcStepType]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcStepType](
	[StepTypeId] [int] IDENTITY(1,1) NOT NULL,
	[StepType] [varchar](50) NOT NULL,
	[FactoryId] [varchar](50) NULL,
 CONSTRAINT [PK_SfcStepType] PRIMARY KEY CLUSTERED 
(
	[StepTypeId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkUnitParameter]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkUnitParameter](
	[UnitParameterId] [int] IDENTITY(1,1) NOT NULL,
	[UnitParameterTagName] [varchar](150) NOT NULL,
	[LabValueName] [varchar](150) NULL,
 CONSTRAINT [PK_TkUnitParameter] PRIMARY KEY CLUSTERED 
(
	[UnitParameterId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK1_TkUnitParameter] ON [dbo].[TkUnitParameter] 
(
	[UnitParameterTagName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UIRGline]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[UIRGline](
	[UIRId] [int] IDENTITY(1,1) NOT NULL,
	[PostId] [int] NULL,
	[UIRTitle] [varchar](100) NULL,
	[Originator] [varchar](100) NULL,
	[ReportDate] [datetime] NULL,
	[ManualEntry] [bit] NULL,
	[IncidentStart] [datetime] NULL,
	[IncidentEnd] [datetime] NULL,
	[Reviewer] [varchar](100) NULL,
	[Grade] [varchar](10) NULL,
	[UnitsAffected] [varchar](2000) NULL,
	[Summary] [varchar](2000) NULL,
	[UIRNumber] [varchar](100) NULL,
	[Area] [varchar](50) NULL,
	[Category] [varchar](50) NULL,
	[FollowUp] [varchar](50) NULL,
	[TimeBasis] [varchar](10) NULL,
	[TextReport] [nvarchar](max) NULL,
	[XMLReport] [nvarchar](max) NULL,
 CONSTRAINT [PK_UIRGline] PRIMARY KEY CLUSTERED 
(
	[UIRId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkWriteLocation]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkWriteLocation](
	[WriteLocationId] [int] IDENTITY(1,1) NOT NULL,
	[Alias] [varchar](max) NOT NULL,
	[ServerName] [varchar](max) NOT NULL,
	[ScanClass] [varchar](max) NOT NULL,
 CONSTRAINT [PK_RecipeServerMap] PRIMARY KEY CLUSTERED 
(
	[WriteLocationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkSite]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkSite](
	[SiteName] [varchar](50) NOT NULL,
	[GatewayStartupScript] [varchar](500) NOT NULL,
	[ClientStartupScript] [varchar](500) NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[VersionLog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[VersionLog](
	[Version] [nchar](10) NOT NULL,
	[ChangeDate] [datetime] NOT NULL,
	[ChangeDetail] [varchar](1000) NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[Version]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Version](
	[Version] [nchar](10) NULL,
	[ReleaseDate] [datetime] NULL
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Units]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Units](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[name] [varchar](64) NOT NULL,
	[isBaseUnit] [bit] NOT NULL,
	[type] [varchar](64) NOT NULL,
	[description] [varchar](2000) NULL,
	[m] [float] NULL,
	[b] [float] NULL,
 CONSTRAINT [PK_Units] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_Units] ON [dbo].[Units] 
(
	[name] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UIRVistalon]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[UIRVistalon](
	[UIRId] [int] IDENTITY(1,1) NOT NULL,
	[FormName] [varchar](100) NULL,
	[PostId] [int] NULL,
	[Title] [varchar](100) NULL,
	[Originator] [varchar](100) NULL,
	[RecordDate] [datetime] NULL,
	[Operator] [varchar](100) NULL,
	[ManualEntry] [bit] NULL,
	[Type] [varchar](100) NULL,
	[IncidentStart] [datetime] NULL,
	[IncidentEnd] [datetime] NULL,
	[Reviewer] [varchar](100) NULL,
	[Grade] [float] NULL,
	[Quality] [varchar](1000) NULL,
	[Root] [varchar](1000) NULL,
	[IncidentSummary] [varchar](2000) NULL,
	[RootExplanation] [varchar](2000) NULL,
	[GradeSummary] [varchar](2000) NULL,
	[CorrectiveAction] [varchar](2000) NULL,
	[BoxLine] [varchar](100) NULL,
	[BoxStart] [varchar](100) NULL,
	[BoxEnd] [varchar](100) NULL,
	[CloseOut] [bit] NULL,
	[Post] [varchar](100) NULL,
	[UIRNumber] [varchar](100) NULL,
 CONSTRAINT [PK_UIRVistalon] PRIMARY KEY CLUSTERED 
(
	[UIRId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[UIRHB]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[UIRHB](
	[UIRId] [int] IDENTITY(1,1) NOT NULL,
	[FormName] [varchar](100) NULL,
	[PostId] [int] NULL,
	[Title] [varchar](100) NULL,
	[Originator] [varchar](100) NULL,
	[RecordDate] [datetime] NULL,
	[Operator] [varchar](100) NULL,
	[ManualEntry] [bit] NULL,
	[Type] [varchar](100) NULL,
	[IncidentStart] [datetime] NULL,
	[IncidentEnd] [datetime] NULL,
	[Reviewer] [varchar](100) NULL,
	[Grade] [float] NULL,
	[Quality] [varchar](1000) NULL,
	[Root] [varchar](1000) NULL,
	[IncidentSummary] [varchar](2000) NULL,
	[RootExplanation] [varchar](2000) NULL,
	[GradeSummary] [varchar](2000) NULL,
	[CorrectiveAction] [varchar](2000) NULL,
	[BoxFlagStart] [varchar](100) NULL,
	[BoxFlagEnd] [varchar](100) NULL,
	[FactStart] [datetime] NULL,
	[FactEnd] [datetime] NULL,
	[CloseOut] [bit] NULL,
	[Post] [varchar](100) NULL,
	[UIRNumber] [varchar](100) NULL,
 CONSTRAINT [PK_UIRHB] PRIMARY KEY CLUSTERED 
(
	[UIRId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[UnitAliases]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[UnitAliases](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[alias] [varchar](64) NOT NULL,
	[name] [varchar](64) NOT NULL,
 CONSTRAINT [PK_UnitAliases] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_UnitAliases] ON [dbo].[UnitAliases] 
(
	[alias] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[UIRGlineInvolvedProperty]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[UIRGlineInvolvedProperty](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[UIRId] [int] NOT NULL,
	[PropertyName] [varchar](50) NOT NULL,
 CONSTRAINT [PK_UIRGlineDetails] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkUnitParameterBuffer]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[TkUnitParameterBuffer](
	[UnitParameterId] [int] NOT NULL,
	[BufferIndex] [int] NOT NULL,
	[RawValue] [float] NULL,
	[SampleTime] [datetime] NULL,
	[ReceiptTime] [datetime] NULL,
 CONSTRAINT [PK_TkUnitParameterBuffer] PRIMARY KEY CLUSTERED 
(
	[UnitParameterId] ASC,
	[BufferIndex] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcStep]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcStep](
	[StepId] [int] IDENTITY(1,1) NOT NULL,
	[StepUUID] [varchar](256) NOT NULL,
	[StepName] [varchar](500) NOT NULL,
	[StepTypeId] [int] NOT NULL,
	[ChartId] [int] NOT NULL,
 CONSTRAINT [PK_SfcStep] PRIMARY KEY CLUSTERED 
(
	[StepId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UK_ChartId_StepName] UNIQUE NONCLUSTERED 
(
	[ChartId] ASC,
	[StepName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkPost]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkPost](
	[PostId] [int] IDENTITY(1,1) NOT NULL,
	[Post] [varchar](50) NOT NULL,
	[MessageQueueId] [int] NOT NULL,
	[LogbookId] [int] NOT NULL,
	[DownloadActive] [bit] NOT NULL,
 CONSTRAINT [PK_TkPost] PRIMARY KEY CLUSTERED 
(
	[PostId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkMessageReply]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkMessageReply](
	[ReplyId] [int] IDENTITY(1,1) NOT NULL,
	[RequestId] [int] NOT NULL,
	[Reply] [varchar](2000) NOT NULL,
	[ReplyTime] [datetime] NOT NULL,
	[ClientId] [varchar](500) NOT NULL,
	[IsolationMode] [bit] NOT NULL,
 CONSTRAINT [PK_TkMessageReply] PRIMARY KEY CLUSTERED 
(
	[ReplyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[TkLogbookDetail]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkLogbookDetail](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[LogbookId] [int] NULL,
	[Timestamp] [datetime] NULL,
	[Message] [varchar](2000) NULL,
 CONSTRAINT [PK_TkLogbookDetail] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataKeyDetail]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataKeyDetail](
	[KeyId] [int] NOT NULL,
	[KeyValue] [varchar](20) NOT NULL,
	[KeyIndex] [int] NOT NULL,
 CONSTRAINT [PK_SfcRecipeDataKeyDetail] PRIMARY KEY CLUSTERED 
(
	[KeyId] ASC,
	[KeyValue] ASC,
	[KeyIndex] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [IX_SfcRecipeDataKeyDetail] ON [dbo].[SfcRecipeDataKeyDetail] 
(
	[KeyId] ASC,
	[KeyValue] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcHierarchyHandler]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcHierarchyHandler](
	[HierarchyId] [int] IDENTITY(1,1) NOT NULL,
	[ChartId] [int] NOT NULL,
	[Handler] [varchar](50) NOT NULL,
	[HandlerChartId] [int] NOT NULL,
 CONSTRAINT [PK_SfcChartHandler] PRIMARY KEY CLUSTERED 
(
	[HierarchyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[QueueDetail]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[QueueDetail](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[QueueId] [int] NOT NULL,
	[Timestamp] [datetime] NOT NULL,
	[StatusId] [int] NOT NULL,
	[Message] [nvarchar](2000) NOT NULL,
 CONSTRAINT [PK_QueueDetail] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Lookup]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[Lookup](
	[LookupId] [int] IDENTITY(1,1) NOT NULL,
	[LookupTypeCode] [varchar](15) NOT NULL,
	[LookupName] [varchar](50) NOT NULL,
	[LookupDescription] [varchar](500) NULL,
	[Active] [bit] NOT NULL,
 CONSTRAINT [PK_Lookup] PRIMARY KEY CLUSTERED 
(
	[LookupId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[DtTextRecommendation]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtTextRecommendation](
	[TextRecommendationId] [int] IDENTITY(1,1) NOT NULL,
	[DiagnosisEntryId] [int] NOT NULL,
	[TextRecommendation] [varchar](2500) NOT NULL,
 CONSTRAINT [PK_DtTextRecommendation] PRIMARY KEY CLUSTERED 
(
	[TextRecommendationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[BtBatchRun]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BtBatchRun](
	[BatchRunId] [int] IDENTITY(1000,1) NOT NULL,
	[ReactorId] [int] NOT NULL,
	[Grade] [float] NOT NULL,
	[BatchCount] [int] NOT NULL,
	[StartDate] [datetime] NOT NULL,
	[EndDate] [datetime] NULL,
 CONSTRAINT [PK_BtBatchRun] PRIMARY KEY CLUSTERED 
(
	[BatchRunId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[BtStripperBatchLog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BtStripperBatchLog](
	[BatchId] [int] IDENTITY(1,1) NOT NULL,
	[BatchRunId] [int] NULL,
	[BatchNumber] [int] NULL,
	[BatchCount] [int] NULL,
	[Status] [varchar](50) NULL,
	[CreationTime] [datetime] NULL,
	[LabResult] [float] NULL,
	[FillBegin] [datetime] NULL,
	[FillEnd] [datetime] NULL,
	[FillTime] [time](7) NULL,
	[StripBegin] [datetime] NULL,
	[StripEnd] [datetime] NULL,
	[StripTime] [time](7) NULL,
	[JD03Begin] [datetime] NULL,
	[JD03End] [datetime] NULL,
	[JD03Time] [time](7) NULL,
	[TransferBegin] [datetime] NULL,
	[TransferEnd] [datetime] NULL,
	[TransferTime] [time](7) NULL,
	[StandbyBegin] [datetime] NULL,
	[StandbyEnd] [datetime] NULL,
	[StandbyTime] [time](7) NULL,
	[TotalStripperTime] [time](7) NULL,
	[TotalChargeAmount] [float] NULL,
 CONSTRAINT [PK_BtStripperBatchLog] PRIMARY KEY CLUSTERED 
(
	[BatchId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[BtBatchLog]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[BtBatchLog](
	[BatchId] [int] IDENTITY(1,1) NOT NULL,
	[BatchRunId] [int] NULL,
	[BatchNumber] [int] NULL,
	[BatchCount] [int] NULL,
	[Status] [varchar](50) NULL,
	[CreationTime] [datetime] NULL,
	[LabResult] [float] NULL,
	[ChargeBegin] [datetime] NULL,
	[ChargeEnd] [datetime] NULL,
	[ChargeTime] [time](7) NULL,
	[HeatUpBegin] [datetime] NULL,
	[HeatUpEnd] [datetime] NULL,
	[HeatUpTime] [time](7) NULL,
	[SoakBegin] [datetime] NULL,
	[SoakEnd] [datetime] NULL,
	[SoakTime] [time](7) NULL,
	[TransferBegin] [datetime] NULL,
	[TransferEnd] [datetime] NULL,
	[TransferTime] [time](7) NULL,
	[StandbyBegin] [datetime] NULL,
	[StandbyEnd] [datetime] NULL,
	[StandbyTime] [time](7) NULL,
	[TotalBatchTime] [time](7) NULL,
	[TotalChargeAmount] [float] NULL,
	[AverageSoakTemp] [float] NULL,
	[SoakTimer] [float] NULL,
 CONSTRAINT [PK_BtBatchLog] PRIMARY KEY CLUSTERED 
(
	[BatchId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtDisplayTable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtDisplayTable](
	[DisplayTableId] [int] IDENTITY(1000,1) NOT NULL,
	[DisplayTableTitle] [varchar](50) NOT NULL,
	[DisplayPage] [int] NOT NULL,
	[DisplayOrder] [int] NOT NULL,
	[DisplayFlag] [bit] NOT NULL,
	[PostId] [int] NOT NULL,
	[OldTableName] [varchar](50) NULL,
 CONSTRAINT [PK_LtConsoles] PRIMARY KEY CLUSTERED 
(
	[DisplayTableId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_LtConsoles] ON [dbo].[LtDisplayTable] 
(
	[DisplayTableTitle] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtRecipeFamily](
	[RecipeFamilyId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeFamilyName] [varchar](50) NOT NULL,
	[RecipeUnitPrefix] [varchar](50) NULL,
	[RecipeNameAlias] [varchar](50) NULL,
	[PostId] [int] NULL,
	[CurrentGrade] [varchar](50) NULL,
	[CurrentVersion] [int] NULL,
	[Status] [varchar](50) NULL,
	[ConfirmDownload] [bit] NULL,
	[Timestamp] [datetime] NULL,
	[Comment] [varchar](2000) NULL,
 CONSTRAINT [PK_RtRecipeMap] PRIMARY KEY CLUSTERED 
(
	[RecipeFamilyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [IX_RtRecipeFamily] ON [dbo].[RtRecipeFamily] 
(
	[RecipeFamilyName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeData](
	[RecipeDataId] [int] IDENTITY(1,1) NOT NULL,
	[StepId] [int] NOT NULL,
	[RecipeDataKey] [varchar](50) NOT NULL,
	[RecipeDataTypeId] [int] NOT NULL,
	[Label] [varchar](100) NULL,
	[Description] [varchar](1000) NULL,
	[Units] [varchar](50) NULL,
	[RecipeDataFolderId] [int] NULL,
 CONSTRAINT [PK_SfcRecipeData] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [UK_SfcRecipeData] UNIQUE NONCLUSTERED 
(
	[StepId] ASC,
	[RecipeDataKey] ASC,
	[RecipeDataFolderId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcControlPanel]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcControlPanel](
	[ControlPanelId] [int] IDENTITY(1,1) NOT NULL,
	[ControlPanelName] [varchar](900) NOT NULL,
	[PostId] [int] NULL,
	[ChartPath] [varchar](900) NOT NULL,
	[ChartRunId] [varchar](900) NULL,
	[Operation] [varchar](900) NULL,
	[MsgQueue] [varchar](900) NULL,
	[Originator] [varchar](900) NULL,
	[Project] [varchar](900) NULL,
	[IsolationMode] [bit] NULL,
	[EnableCancel] [bit] NULL,
	[EnablePause] [bit] NULL,
	[EnableReset] [bit] NULL,
	[EnableResume] [bit] NULL,
	[EnableStart] [bit] NULL,
 CONSTRAINT [PK_SfcControlPanelPete] PRIMARY KEY CLUSTERED 
(
	[ControlPanelId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_SfcControlPanel] ON [dbo].[SfcControlPanel] 
(
	[ControlPanelName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[SfcChartStepView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcChartStepView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepName, dbo.SfcStep.StepUUID, dbo.SfcStepType.StepType
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcStepType ON dbo.SfcStep.StepTypeId = dbo.SfcStepType.StepTypeId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[20] 2[11] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 145
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 157
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStepType"
            Begin Extent = 
               Top = 6
               Left = 445
               Bottom = 111
               Right = 605
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcChartStepView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcChartStepView'
GO
/****** Object:  Table [dbo].[SfcHierarchy]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcHierarchy](
	[HierarchyId] [int] IDENTITY(200,1) NOT NULL,
	[StepId] [int] NOT NULL,
	[ChartId] [int] NOT NULL,
	[ChildChartId] [int] NOT NULL,
 CONSTRAINT [PK_SfcHierarchy] PRIMARY KEY CLUSTERED 
(
	[HierarchyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY],
 CONSTRAINT [IX_SfcHierarchy] UNIQUE NONCLUSTERED 
(
	[StepId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[SfcRecipeDataKeyView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataKeyView]
AS
SELECT     dbo.SfcRecipeDataKeyMaster.KeyId, dbo.SfcRecipeDataKeyMaster.KeyName, dbo.SfcRecipeDataKeyDetail.KeyValue, dbo.SfcRecipeDataKeyDetail.KeyIndex
FROM         dbo.SfcRecipeDataKeyMaster INNER JOIN
                      dbo.SfcRecipeDataKeyDetail ON dbo.SfcRecipeDataKeyMaster.KeyId = dbo.SfcRecipeDataKeyDetail.KeyId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[20] 2[11] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcRecipeDataKeyMaster"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 95
               Right = 240
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataKeyDetail"
            Begin Extent = 
               Top = 4
               Left = 308
               Bottom = 108
               Right = 511
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 3270
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataKeyView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataKeyView'
GO
/****** Object:  Table [dbo].[SfcRecipeDataFolder]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataFolder](
	[RecipeDataFolderId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeDataKey] [varchar](50) NOT NULL,
	[StepId] [int] NOT NULL,
	[RecipeDataType] [varchar](50) NOT NULL,
	[ParentRecipeDataFolderId] [int] NULL,
	[Description] [varchar](1000) NULL,
	[Label] [varchar](100) NULL,
 CONSTRAINT [PK_SfcRecipeDataFolder] PRIMARY KEY CLUSTERED 
(
	[RecipeDataFolderId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[SfcHierarchyHandlerView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcHierarchyHandlerView]
AS
SELECT     dbo.SfcHierarchyHandler.HierarchyId, dbo.SfcHierarchyHandler.ChartId, dbo.SfcChart.ChartPath, dbo.SfcHierarchyHandler.Handler, dbo.SfcHierarchyHandler.HandlerChartId, 
                      SfcChart_1.ChartPath AS HandlerChartPath, dbo.SfcChart.IsProduction
FROM         dbo.SfcHierarchyHandler INNER JOIN
                      dbo.SfcChart ON dbo.SfcHierarchyHandler.ChartId = dbo.SfcChart.ChartId INNER JOIN
                      dbo.SfcChart AS SfcChart_1 ON dbo.SfcHierarchyHandler.HandlerChartId = SfcChart_1.ChartId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcHierarchyHandler"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 126
               Right = 201
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 3
               Left = 279
               Bottom = 164
               Right = 450
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcChart_1"
            Begin Extent = 
               Top = 193
               Left = 312
               Bottom = 313
               Right = 483
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 4590
         Width = 1500
         Width = 1500
         Width = 7365
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcHierarchyHandlerView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcHierarchyHandlerView'
GO
/****** Object:  Table [dbo].[TkConsole]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkConsole](
	[ConsoleId] [int] IDENTITY(1000,1) NOT NULL,
	[WindowName] [varchar](100) NOT NULL,
	[ConsoleName] [varchar](100) NOT NULL,
	[Priority] [int] NOT NULL,
	[PostId] [int] NOT NULL,
 CONSTRAINT [PK_DtConsole] PRIMARY KEY CLUSTERED 
(
	[ConsoleId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK1_TkConsole] ON [dbo].[TkConsole] 
(
	[ConsoleName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK2_TkConsole] ON [dbo].[TkConsole] 
(
	[WindowName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[SfcStepView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcStepView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepName, dbo.SfcStep.StepUUID, dbo.SfcStepType.StepType
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcStepType ON dbo.SfcStep.StepTypeId = dbo.SfcStepType.StepTypeId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[20] 2[11] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 145
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 157
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStepType"
            Begin Extent = 
               Top = 6
               Left = 445
               Bottom = 111
               Right = 605
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcStepView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcStepView'
GO
/****** Object:  Table [dbo].[TkUnit]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[TkUnit](
	[UnitId] [int] IDENTITY(1,1) NOT NULL,
	[UnitName] [varchar](50) NOT NULL,
	[UnitPrefix] [varchar](50) NULL,
	[UnitAlias] [varchar](50) NULL,
	[PostId] [int] NOT NULL,
	[Grade] [varchar](50) NULL,
 CONSTRAINT [PK__UnitRoot__44F5ECB5173876EA] PRIMARY KEY CLUSTERED 
(
	[UnitId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [Uk_TkUnit_UnitName] ON [dbo].[TkUnit] 
(
	[UnitName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[TkPostView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[TkPostView]
AS
SELECT     dbo.TkPost.PostId, dbo.TkPost.Post, dbo.QueueMaster.QueueId, dbo.QueueMaster.QueueKey, dbo.TkLogbook.LogbookId, dbo.TkLogbook.logbookName, dbo.TkPost.DownloadActive
FROM         dbo.TkPost INNER JOIN
                      dbo.QueueMaster ON dbo.TkPost.MessageQueueId = dbo.QueueMaster.QueueId LEFT OUTER JOIN
                      dbo.TkLogbook ON dbo.TkPost.LogbookId = dbo.TkLogbook.LogbookId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 168
               Right = 211
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkLogbook"
            Begin Extent = 
               Top = 183
               Left = 280
               Bottom = 288
               Right = 448
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "QueueMaster"
            Begin Extent = 
               Top = 17
               Left = 320
               Bottom = 157
               Right = 513
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'TkPostView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'TkPostView'
GO
/****** Object:  View [dbo].[UnitAliasesView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[UnitAliasesView]
AS
SELECT     dbo.UnitAliases.id, dbo.UnitAliases.alias AS name, dbo.Units.isBaseUnit, dbo.Units.type, dbo.Units.description, dbo.Units.m, dbo.Units.b
FROM         dbo.UnitAliases INNER JOIN
                      dbo.Units ON dbo.UnitAliases.name = dbo.Units.name
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "UnitAliases"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 154
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "Units"
            Begin Extent = 
               Top = 6
               Left = 236
               Bottom = 207
               Right = 396
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'UnitAliasesView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'UnitAliasesView'
GO
/****** Object:  View [dbo].[UIRGlineView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[UIRGlineView]
AS
SELECT     dbo.UIRGline.UIRId, dbo.UIRGline.UIRNumber, dbo.UIRGline.UIRTitle, dbo.TkPost.Post, dbo.UIRGline.Originator, dbo.UIRGline.ReportDate, dbo.UIRGline.IncidentStart, dbo.UIRGline.IncidentEnd, 
                      dbo.UIRGline.Grade, dbo.UIRGline.Area, dbo.UIRGline.Category, dbo.UIRGline.TimeBasis, dbo.UIRGline.FollowUp, dbo.UIRGline.UnitsAffected, dbo.UIRGline.Summary, dbo.UIRGline.ManualEntry, 
                      dbo.UIRGline.Reviewer
FROM         dbo.UIRGline INNER JOIN
                      dbo.TkPost ON dbo.UIRGline.PostId = dbo.TkPost.PostId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1[50] 4[25] 3) )"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 1
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "UIRGline"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 330
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 6
               Left = 236
               Bottom = 126
               Right = 409
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
      PaneHidden = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'UIRGlineView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'UIRGlineView'
GO
/****** Object:  View [dbo].[UnitsView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[UnitsView]
AS
SELECT   * from Units
UNION
SELECT * from UnitAliasesView
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'UnitsView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'UnitsView'
GO
/****** Object:  View [dbo].[TkUnitView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[TkUnitView]
AS
SELECT     dbo.TkUnit.UnitId, dbo.TkUnit.UnitName, dbo.TkUnit.UnitPrefix, dbo.TkUnit.UnitAlias, dbo.TkPost.PostId, dbo.TkPost.Post
FROM         dbo.TkUnit LEFT OUTER JOIN
                      dbo.TkPost ON dbo.TkUnit.PostId = dbo.TkPost.PostId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[20] 2[9] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 66
               Left = 288
               Bottom = 207
               Right = 461
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 163
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'TkUnitView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'TkUnitView'
GO
/****** Object:  View [dbo].[TkConsoleView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[TkConsoleView]
AS
SELECT     dbo.TkConsole.ConsoleId, dbo.TkConsole.ConsoleName, dbo.TkConsole.WindowName, dbo.TkConsole.Priority, dbo.TkPost.PostId, dbo.TkPost.Post
FROM         dbo.TkConsole INNER JOIN
                      dbo.TkPost ON dbo.TkConsole.PostId = dbo.TkPost.PostId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "TkConsole"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 159
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 6
               Left = 236
               Bottom = 152
               Right = 409
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'TkConsoleView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'TkConsoleView'
GO
/****** Object:  Table [dbo].[SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcWindow](
	[windowId] [int] IDENTITY(1,1) NOT NULL,
	[chartRunId] [varchar](900) NOT NULL,
	[controlPanelId] [int] NOT NULL,
	[windowPath] [varchar](64) NOT NULL,
	[buttonLabel] [varchar](64) NOT NULL,
	[position] [varchar](100) NOT NULL,
	[scale] [float] NOT NULL,
	[title] [varchar](100) NOT NULL,
 CONSTRAINT [PK_SfcWindow_1] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[SfcRecipeDataView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, dbo.SfcRecipeData.RecipeDataFolderId
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeData INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId ON dbo.SfcStep.StepId = dbo.SfcRecipeData.StepId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[30] 4[32] 2[14] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 36
               Left = 21
               Bottom = 141
               Right = 192
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 27
               Left = 247
               Bottom = 182
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 13
               Left = 490
               Bottom = 210
               Right = 668
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 79
               Left = 735
               Bottom = 210
               Right = 913
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 12
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2910
         Alias = 4035
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
        ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N' Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataView'
GO
/****** Object:  Table [dbo].[SfcRecipeDataSQC]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataSQC](
	[RecipeDataId] [int] NOT NULL,
	[LowLimit] [float] NULL,
	[TargetValue] [float] NULL,
	[HighLimit] [float] NULL,
 CONSTRAINT [PK_SfcRecipeDataSQC] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcRecipeDataTimer]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataTimer](
	[RecipeDataId] [int] NOT NULL,
	[StartTime] [datetime] NULL,
	[StopTime] [datetime] NULL,
	[TimerState] [varchar](10) NULL,
	[CumulativeMinutes] [float] NULL,
 CONSTRAINT [PK_SfcRecipeDataTimer] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtDownloadMaster]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtDownloadMaster](
	[MasterId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeFamilyId] [int] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[Version] [int] NOT NULL,
	[Type] [varchar](50) NULL,
	[DownloadStartTime] [datetime] NOT NULL,
	[DownloadEndTime] [datetime] NULL,
	[Status] [varchar](50) NULL,
	[TotalDownloads] [int] NULL,
	[PassedDownloads] [int] NULL,
	[FailedDownloads] [int] NULL,
 CONSTRAINT [PK_DownloadMaster] PRIMARY KEY CLUSTERED 
(
	[MasterId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[SfcHierarchyView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcHierarchyView]
AS
SELECT     dbo.SfcHierarchy.HierarchyId, dbo.SfcHierarchy.StepId, dbo.SfcStepType.StepType, dbo.SfcHierarchy.ChartId, dbo.SfcHierarchy.ChildChartId, dbo.SfcChart.ChartPath, dbo.SfcChart.ChartResourceId, 
                      SfcChart_1.ChartPath AS ChildChartPath, SfcChart_1.ChartResourceId AS ChildResourceId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcStep.StepTypeId, dbo.SfcChart.IsProduction
FROM         dbo.SfcHierarchy INNER JOIN
                      dbo.SfcChart ON dbo.SfcHierarchy.ChartId = dbo.SfcChart.ChartId INNER JOIN
                      dbo.SfcChart AS SfcChart_1 ON dbo.SfcHierarchy.ChildChartId = SfcChart_1.ChartId INNER JOIN
                      dbo.SfcStep ON dbo.SfcHierarchy.StepId = dbo.SfcStep.StepId AND dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcStepType ON dbo.SfcStep.StepTypeId = dbo.SfcStepType.StepTypeId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[37] 4[32] 2[14] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1[35] 4[35] 3) )"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 1
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcHierarchy"
            Begin Extent = 
               Top = 193
               Left = 501
               Bottom = 313
               Right = 661
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 13
               Left = 12
               Bottom = 165
               Right = 172
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcChart_1"
            Begin Extent = 
               Top = 178
               Left = 8
               Bottom = 283
               Right = 168
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 3
               Left = 314
               Bottom = 155
               Right = 474
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStepType"
            Begin Extent = 
               Top = 50
               Left = 626
               Bottom = 160
               Right = 786
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
      PaneHidden = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 12
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcHierarchyView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2385
         Alias = 1500
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcHierarchyView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcHierarchyView'
GO
/****** Object:  Table [dbo].[SfcRecipeDataInput]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataInput](
	[RecipeDataId] [int] NOT NULL,
	[ValueTypeId] [int] NULL,
	[Tag] [varchar](500) NULL,
	[ErrorCode] [varchar](50) NULL,
	[ErrorText] [varchar](5000) NULL,
	[PVMonitorActive] [bit] NULL,
	[PVMonitorStatus] [varchar](25) NULL,
	[PVValueId] [int] NULL,
	[TargetValueId] [int] NULL,
 CONSTRAINT [PK_SfcRecipeDataInput] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[SfcRecipeDataFolderView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataFolderView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeDataFolder.RecipeDataFolderId, 
                      dbo.SfcRecipeDataFolder.RecipeDataKey, dbo.SfcRecipeDataFolder.ParentRecipeDataFolderId, dbo.SfcRecipeDataFolder.Description, dbo.SfcRecipeDataFolder.Label, 
                      dbo.SfcRecipeDataFolder.RecipeDataType
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcStep.StepId = dbo.SfcRecipeDataFolder.StepId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[28] 4[33] 2[12] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 158
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 148
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 14
               Left = 459
               Bottom = 188
               Right = 675
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 11
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1800
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2625
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataFolderView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataFolderView'
GO
/****** Object:  Table [dbo].[SfcRecipeDataArray]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataArray](
	[RecipeDataId] [int] NOT NULL,
	[ValueTypeId] [int] NOT NULL,
	[IndexKeyId] [int] NULL,
 CONSTRAINT [PK_SfcRecipeDataArray] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcRecipeDataMatrixElement]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataMatrixElement](
	[RecipeDataId] [int] NOT NULL,
	[RowIndex] [int] NOT NULL,
	[ColumnIndex] [int] NOT NULL,
	[ValueId] [int] NOT NULL,
 CONSTRAINT [PK_SfcRecipeDataMatrixElement] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC,
	[RowIndex] ASC,
	[ColumnIndex] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcRecipeDataMatrix]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataMatrix](
	[RecipeDataId] [int] NOT NULL,
	[ValueTypeId] [int] NOT NULL,
	[Rows] [int] NOT NULL,
	[Columns] [int] NOT NULL,
	[RowIndexKeyId] [int] NULL,
	[ColumnIndexKeyId] [int] NULL,
 CONSTRAINT [PK_SfcRecipeDataMatrix] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcRecipeDataOutputRamp]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataOutputRamp](
	[RecipeDataId] [int] NOT NULL,
	[RampTimeMinutes] [float] NULL,
	[UpdateFrequencySeconds] [float] NULL,
 CONSTRAINT [PK_SfcRecipeDataOutputRamp] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcRecipeDataOutput]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataOutput](
	[RecipeDataId] [int] NOT NULL,
	[ValueTypeId] [int] NOT NULL,
	[OutputTypeId] [int] NOT NULL,
	[Tag] [varchar](500) NULL,
	[Download] [bit] NULL,
	[DownloadStatus] [varchar](50) NULL,
	[ErrorCode] [varchar](50) NULL,
	[ErrorText] [varchar](1000) NULL,
	[Timing] [float] NULL,
	[MaxTiming] [float] NULL,
	[ActualTiming] [float] NULL,
	[ActualDateTime] [datetime] NULL,
	[OutputValueId] [int] NOT NULL,
	[TargetValueId] [int] NOT NULL,
	[PVValueId] [int] NOT NULL,
	[PVMonitorActive] [bit] NULL,
	[PVMonitorStatus] [varchar](50) NULL,
	[SetpointStatus] [varchar](50) NULL,
	[WriteConfirm] [bit] NULL,
	[WriteConfirmed] [bit] NULL,
 CONSTRAINT [PK_SfcRecipeDataOutput] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataRecipe]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcRecipeDataRecipe](
	[RecipeDataId] [int] NOT NULL,
	[PresentationOrder] [int] NOT NULL,
	[StoreTag] [varchar](max) NULL,
	[CompareTag] [varchar](max) NULL,
	[ModeAttribute] [varchar](max) NULL,
	[ModeValue] [varchar](max) NULL,
	[ChangeLevel] [varchar](max) NULL,
	[RecommendedValue] [varchar](max) NULL,
	[LowLimit] [varchar](max) NULL,
	[HighLimit] [varchar](max) NULL,
 CONSTRAINT [PK_SfcRecipeDataRecipe] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcRecipeDataSimpleValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataSimpleValue](
	[RecipeDataId] [int] NOT NULL,
	[ValueTypeId] [int] NULL,
	[ValueId] [int] NULL,
 CONSTRAINT [PK_SfcRecipeDataSimpleValue] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcControlPanelMessage]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcControlPanelMessage](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[controlPanelId] [int] NOT NULL,
	[message] [varchar](256) NOT NULL,
	[priority] [varchar](20) NOT NULL,
	[createTime] [datetime] NOT NULL,
	[ackRequired] [bit] NOT NULL,
 CONSTRAINT [PK_SfcControlPanelMessage] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE NONCLUSTERED INDEX [idx_control_msgs] ON [dbo].[SfcControlPanelMessage] 
(
	[controlPanelId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtValueDefinition]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtValueDefinition](
	[RecipeFamilyId] [int] NOT NULL,
	[ValueId] [int] IDENTITY(1,1) NOT NULL,
	[PresentationOrder] [int] NOT NULL,
	[Description] [nvarchar](max) NULL,
	[StoreTag] [varchar](max) NULL,
	[CompareTag] [varchar](max) NULL,
	[ChangeLevel] [nvarchar](max) NULL,
	[ModeAttribute] [varchar](max) NULL,
	[ModeValue] [varchar](max) NULL,
	[WriteLocationId] [int] NULL,
	[ValueTypeId] [int] NULL,
 CONSTRAINT [PK__ValueDef__93364E481B0907CE] PRIMARY KEY CLUSTERED 
(
	[RecipeFamilyId] ASC,
	[ValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtSQCParameter]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtSQCParameter](
	[ParameterId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeFamilyId] [int] NOT NULL,
	[Parameter] [varchar](max) NOT NULL,
 CONSTRAINT [PK_RtSQCParameter] PRIMARY KEY CLUSTERED 
(
	[ParameterId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtGain]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[RtGain](
	[ParameterId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeFamilyId] [int] NULL,
	[Parameter] [nvarchar](max) NOT NULL,
 CONSTRAINT [PK__Gain__F80C6277286302EC] PRIMARY KEY CLUSTERED 
(
	[ParameterId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtEventParameter]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtEventParameter](
	[ParameterId] [int] IDENTITY(1,1) NOT NULL,
	[RecipeFamilyId] [int] NOT NULL,
	[Parameter] [varchar](max) NOT NULL,
 CONSTRAINT [PK_RtEventParameter] PRIMARY KEY CLUSTERED 
(
	[ParameterId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtGradeMaster]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtGradeMaster](
	[RecipeFamilyId] [int] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[Version] [int] NOT NULL,
	[Timestamp] [datetime] NULL,
	[Active] [bit] NULL,
 CONSTRAINT [PK_RtGradeMaster] PRIMARY KEY CLUSTERED 
(
	[RecipeFamilyId] ASC,
	[Grade] ASC,
	[Version] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtGradeDetail]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtGradeDetail](
	[RecipeFamilyId] [int] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[Version] [int] NOT NULL,
	[ValueId] [int] NOT NULL,
	[RecommendedValue] [varchar](max) NULL,
	[LowLimit] [varchar](max) NULL,
	[HighLimit] [varchar](max) NULL,
 CONSTRAINT [PK_RtGradeDetail] PRIMARY KEY CLUSTERED 
(
	[RecipeFamilyId] ASC,
	[Grade] ASC,
	[Version] ASC,
	[ValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtValue](
	[ValueId] [int] IDENTITY(1000,1) NOT NULL,
	[ValueName] [nvarchar](50) NOT NULL,
	[UnitId] [int] NOT NULL,
	[DisplayDecimals] [int] NOT NULL,
	[Description] [nvarchar](500) NULL,
	[ValidationProcedure] [varchar](250) NULL,
	[LastHistoryId] [int] NULL,
 CONSTRAINT [PK_LtLabValue] PRIMARY KEY CLUSTERED 
(
	[ValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_LtLabValue] ON [dbo].[LtValue] 
(
	[ValueName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DtApplication]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtApplication](
	[ApplicationId] [int] IDENTITY(1,1) NOT NULL,
	[ApplicationName] [varchar](250) NOT NULL,
	[UnitId] [int] NULL,
	[Description] [varchar](2000) NULL,
	[IncludeInMainMenu] [bit] NULL,
	[MessageQueueId] [int] NULL,
	[GroupRampMethodId] [int] NULL,
	[DownloadAction] [varchar](50) NULL,
	[NotificationStrategy] [varchar](50) NOT NULL,
	[ClientId] [varchar](50) NULL,
	[Managed] [bit] NOT NULL,
 CONSTRAINT [PK_DtApplication] PRIMARY KEY CLUSTERED 
(
	[ApplicationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_Application] ON [dbo].[DtApplication] 
(
	[ApplicationName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[BtBatchView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[BtBatchView]
AS
SELECT     dbo.BtReactor.ReactorId, dbo.BtReactor.ReactorName, dbo.BtReactor.TagName, dbo.BtBatchRun.BatchRunId, dbo.BtBatchRun.Grade, dbo.BtBatchRun.StartDate, dbo.BtBatchRun.EndDate, 
                      dbo.BtBatchLog.BatchId, dbo.BtBatchLog.BatchNumber, dbo.BtBatchLog.BatchCount, dbo.BtBatchLog.Status, dbo.BtBatchLog.CreationTime, dbo.BtBatchLog.LabResult, 
                      dbo.BtBatchLog.ChargeBegin, dbo.BtBatchLog.ChargeEnd, dbo.BtBatchLog.ChargeTime, dbo.BtBatchLog.HeatUpBegin, dbo.BtBatchLog.HeatUpEnd, dbo.BtBatchLog.HeatUpTime, 
                      dbo.BtBatchLog.SoakBegin, dbo.BtBatchLog.SoakEnd, dbo.BtBatchLog.SoakTime, dbo.BtBatchLog.TransferBegin, dbo.BtBatchLog.TransferEnd, dbo.BtBatchLog.TransferTime, 
                      dbo.BtBatchLog.StandbyBegin, dbo.BtBatchLog.StandbyEnd, dbo.BtBatchLog.StandbyTime, dbo.BtBatchLog.TotalBatchTime, dbo.BtBatchLog.TotalChargeAmount, 
                      dbo.BtBatchLog.AverageSoakTemp, dbo.BtBatchLog.SoakTimer
FROM         dbo.BtReactor INNER JOIN
                      dbo.BtBatchRun ON dbo.BtReactor.ReactorId = dbo.BtBatchRun.ReactorId INNER JOIN
                      dbo.BtBatchLog ON dbo.BtBatchRun.BatchRunId = dbo.BtBatchLog.BatchRunId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "BtReactor"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 122
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "BtBatchRun"
            Begin Extent = 
               Top = 6
               Left = 236
               Bottom = 163
               Right = 396
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "BtBatchLog"
            Begin Extent = 
               Top = 6
               Left = 434
               Bottom = 453
               Right = 619
            End
            DisplayFlags = 280
            TopColumn = 1
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'BtBatchView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'BtBatchView'
GO
/****** Object:  View [dbo].[BtStripperBatchView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[BtStripperBatchView]
AS
SELECT     dbo.BtReactor.ReactorId, dbo.BtReactor.ReactorName, dbo.BtReactor.TagName, dbo.BtBatchRun.BatchRunId, dbo.BtBatchRun.Grade, dbo.BtBatchRun.StartDate, dbo.BtBatchRun.EndDate, 
                      dbo.BtStripperBatchLog.BatchId, dbo.BtStripperBatchLog.BatchNumber, dbo.BtStripperBatchLog.BatchCount, dbo.BtStripperBatchLog.Status, dbo.BtStripperBatchLog.CreationTime, 
                      dbo.BtStripperBatchLog.LabResult, dbo.BtStripperBatchLog.FillBegin, dbo.BtStripperBatchLog.FillEnd, dbo.BtStripperBatchLog.FillTime, dbo.BtStripperBatchLog.StripBegin, 
                      dbo.BtStripperBatchLog.StripEnd, dbo.BtStripperBatchLog.StripTime, dbo.BtStripperBatchLog.JD03Begin, dbo.BtStripperBatchLog.JD03End, dbo.BtStripperBatchLog.JD03Time, 
                      dbo.BtStripperBatchLog.TransferBegin, dbo.BtStripperBatchLog.TransferEnd, dbo.BtStripperBatchLog.TransferTime, dbo.BtStripperBatchLog.StandbyBegin, dbo.BtStripperBatchLog.StandbyEnd, 
                      dbo.BtStripperBatchLog.StandbyTime, dbo.BtStripperBatchLog.TotalStripperTime, dbo.BtStripperBatchLog.TotalChargeAmount
FROM         dbo.BtReactor INNER JOIN
                      dbo.BtBatchRun ON dbo.BtReactor.ReactorId = dbo.BtBatchRun.ReactorId INNER JOIN
                      dbo.BtStripperBatchLog ON dbo.BtBatchRun.BatchRunId = dbo.BtStripperBatchLog.BatchRunId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[33] 2[8] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "BtReactor"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 127
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "BtBatchRun"
            Begin Extent = 
               Top = 6
               Left = 236
               Bottom = 173
               Right = 396
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "BtStripperBatchLog"
            Begin Extent = 
               Top = 6
               Left = 434
               Bottom = 309
               Right = 619
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 2250
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'BtStripperBatchView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'BtStripperBatchView'
GO
/****** Object:  Table [dbo].[LtDerivedValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtDerivedValue](
	[DerivedValueId] [int] IDENTITY(1,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[TriggerValueId] [int] NOT NULL,
	[Callback] [varchar](250) NOT NULL,
	[SampleTimeTolerance] [int] NOT NULL,
	[NewSampleWaitTime] [int] NOT NULL,
	[ResultItemId] [varchar](100) NULL,
	[ResultInterfaceId] [int] NULL,
 CONSTRAINT [PK_LtDerivedValue] PRIMARY KEY CLUSTERED 
(
	[DerivedValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[DtFamily]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtFamily](
	[FamilyId] [int] IDENTITY(1,1) NOT NULL,
	[ApplicationId] [int] NOT NULL,
	[FamilyName] [varchar](250) NOT NULL,
	[FamilyPriority] [float] NOT NULL,
	[Description] [varchar](2000) NULL,
 CONSTRAINT [PK_DtFamily] PRIMARY KEY CLUSTERED 
(
	[FamilyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_DtFamily] ON [dbo].[DtFamily] 
(
	[FamilyName] ASC,
	[ApplicationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DtQuantOutput]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtQuantOutput](
	[QuantOutputId] [int] IDENTITY(1,1) NOT NULL,
	[QuantOutputName] [varchar](750) NOT NULL,
	[ApplicationId] [int] NOT NULL,
	[TagPath] [varchar](1000) NOT NULL,
	[MostNegativeIncrement] [float] NOT NULL,
	[MostPositiveIncrement] [float] NOT NULL,
	[IgnoreMinimumIncrement] [bit] NOT NULL,
	[MinimumIncrement] [float] NOT NULL,
	[SetpointHighLimit] [float] NOT NULL,
	[SetpointLowLimit] [float] NOT NULL,
	[FeedbackMethodId] [int] NOT NULL,
	[IncrementalOutput] [bit] NOT NULL,
	[OutputLimitedStatus] [varchar](50) NULL,
	[OutputLimited] [bit] NULL,
	[OutputPercent] [float] NULL,
	[FeedbackOutput] [float] NULL,
	[FeedbackOutputManual] [float] NULL,
	[FeedbackOutputConditioned] [float] NULL,
	[ManualOverride] [bit] NULL,
	[Active] [bit] NULL,
	[DownloadAction] [varchar](25) NULL,
	[DownloadStatus] [varchar](100) NULL,
	[CurrentSetpoint] [float] NULL,
	[FinalSetpoint] [float] NULL,
	[DisplayedRecommendation] [float] NULL,
 CONSTRAINT [PK_DtQuantOutput] PRIMARY KEY CLUSTERED 
(
	[QuantOutputId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_DtQuantOutput] ON [dbo].[DtQuantOutput] 
(
	[QuantOutputName] ASC,
	[ApplicationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[DtApplicationView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[DtApplicationView]
AS
SELECT     dbo.DtApplication.ApplicationId, dbo.TkPost.Post, dbo.TkUnit.UnitName, dbo.DtApplication.ApplicationName, dbo.DtApplication.Description, dbo.DtApplication.IncludeInMainMenu, 
                      dbo.QueueMaster.QueueKey, dbo.DtApplication.DownloadAction, dbo.DtApplication.NotificationStrategy, dbo.DtApplication.Managed, dbo.Lookup.LookupName AS GroupRampMethodName, 
                      dbo.Lookup.LookupTypeCode AS GroupRampMethodCode
FROM         dbo.DtApplication INNER JOIN
                      dbo.TkUnit ON dbo.DtApplication.UnitId = dbo.TkUnit.UnitId INNER JOIN
                      dbo.QueueMaster ON dbo.DtApplication.MessageQueueId = dbo.QueueMaster.QueueId INNER JOIN
                      dbo.TkPost ON dbo.TkUnit.PostId = dbo.TkPost.PostId INNER JOIN
                      dbo.Lookup ON dbo.DtApplication.GroupRampMethodId = dbo.Lookup.LookupId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[33] 2[8] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "DtApplication"
            Begin Extent = 
               Top = 27
               Left = 405
               Bottom = 261
               Right = 596
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 24
               Left = 213
               Bottom = 183
               Right = 373
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "QueueMaster"
            Begin Extent = 
               Top = 37
               Left = 642
               Bottom = 157
               Right = 863
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 25
               Left = 10
               Bottom = 145
               Right = 183
            End
            DisplayFlags = 280
            TopColumn = 1
         End
         Begin Table = "Lookup"
            Begin Extent = 
               Top = 162
               Left = 639
               Bottom = 282
               Right = 815
            End
            DisplayFlags = 280
            TopColumn = 1
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 2625
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtApplicationView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtApplicationView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtApplicationView'
GO
/****** Object:  Table [dbo].[LtDisplayTableDetails]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LtDisplayTableDetails](
	[Id] [int] IDENTITY(1,1) NOT NULL,
	[DisplayTableId] [int] NOT NULL,
	[ValueId] [int] NOT NULL,
	[DisplayOrder] [int] NOT NULL,
 CONSTRAINT [PK_LtDisplayTableDetails] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LtHistory]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtHistory](
	[HistoryId] [int] IDENTITY(1000,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[RawValue] [float] NOT NULL,
	[SampleTime] [datetime] NOT NULL,
	[ReportTime] [datetime] NOT NULL,
	[Grade] [varchar](50) NULL,
 CONSTRAINT [PK_LtHistory] PRIMARY KEY CLUSTERED 
(
	[HistoryId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_LtHistory] ON [dbo].[LtHistory] 
(
	[ValueId] ASC,
	[RawValue] ASC,
	[SampleTime] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[LtDCSValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtDCSValue](
	[DCSValueId] [int] IDENTITY(1,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[ItemId] [varchar](100) NOT NULL,
	[InterfaceId] [int] NOT NULL,
	[MinimumSampleIntervalSeconds] [int] NOT NULL,
	[AllowManualEntry] [bit] NOT NULL,
 CONSTRAINT [PK_LtDCSValue] PRIMARY KEY CLUSTERED 
(
	[DCSValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtLimit]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtLimit](
	[LimitId] [int] IDENTITY(1000,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[LimitTypeId] [int] NOT NULL,
	[LimitSourceId] [int] NOT NULL,
	[RecipeParameterName] [varchar](100) NULL,
	[UpperValidityLimit] [float] NULL,
	[LowerValidityLimit] [float] NULL,
	[UpperSQCLimit] [float] NULL,
	[LowerSQCLimit] [float] NULL,
	[UpperReleaseLimit] [float] NULL,
	[LowerReleaseLimit] [float] NULL,
	[Target] [float] NULL,
	[StandardDeviation] [float] NULL,
	[OPCUpperItemId] [varchar](50) NULL,
	[OPCLowerItemId] [varchar](50) NULL,
	[OPCInterfaceId] [int] NULL,
 CONSTRAINT [PK_LtLimit] PRIMARY KEY CLUSTERED 
(
	[LimitId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtLocalValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtLocalValue](
	[LocalValueId] [int] IDENTITY(1,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[ItemId] [varchar](500) NULL,
	[InterfaceId] [int] NULL,
 CONSTRAINT [PK_LtLocalValue] PRIMARY KEY CLUSTERED 
(
	[LocalValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtValueViewed]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtValueViewed](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[Username] [varchar](25) NOT NULL,
	[ViewTime] [datetime] NOT NULL,
 CONSTRAINT [PK_LtValueViewed] PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcBusyNotification]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcBusyNotification](
	[windowId] [int] NOT NULL,
	[message] [varchar](900) NULL,
 CONSTRAINT [PK_SfcBusyNotification] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtEvent]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtEvent](
	[ParameterId] [int] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[Value] [float] NULL,
 CONSTRAINT [PK_RtEvent] PRIMARY KEY CLUSTERED 
(
	[ParameterId] ASC,
	[Grade] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtSelector]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LtSelector](
	[SelectorId] [int] IDENTITY(1000,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[hasSQCLimit] [bit] NOT NULL,
	[hasValidityLimit] [bit] NOT NULL,
	[hasReleaseLimit] [bit] NOT NULL,
	[sourceValueId] [int] NULL,
 CONSTRAINT [PK_LtSelector] PRIMARY KEY CLUSTERED 
(
	[SelectorId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[RtGainGrade]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtGainGrade](
	[ParameterId] [int] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[Gain] [float] NULL,
 CONSTRAINT [PK_RtGainGrade] PRIMARY KEY CLUSTERED 
(
	[ParameterId] ASC,
	[Grade] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[LtPHDValue]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[LtPHDValue](
	[PHDValueId] [int] IDENTITY(1,1) NOT NULL,
	[ValueId] [int] NOT NULL,
	[ItemId] [varchar](50) NOT NULL,
	[InterfaceId] [int] NOT NULL,
	[AllowManualEntry] [bit] NOT NULL,
 CONSTRAINT [PK_LtPHDLabValue] PRIMARY KEY CLUSTERED 
(
	[PHDValueId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[RtSQCLimit]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[RtSQCLimit](
	[ParameterId] [int] NOT NULL,
	[Grade] [varchar](50) NOT NULL,
	[UpperLimit] [float] NULL,
	[LowerLimit] [float] NULL,
 CONSTRAINT [PK_RtSQCLimit] PRIMARY KEY CLUSTERED 
(
	[ParameterId] ASC,
	[Grade] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[RtRecipeView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[RtRecipeView]
AS
SELECT     dbo.RtRecipeFamily.RecipeFamilyId, dbo.RtRecipeFamily.RecipeFamilyName, dbo.RtGradeMaster.Grade, dbo.RtGradeMaster.Version, dbo.RtGradeDetail.ValueId, 
                      dbo.RtValueDefinition.PresentationOrder, dbo.RtValueDefinition.Description, dbo.RtValueDefinition.StoreTag, dbo.RtValueDefinition.CompareTag, dbo.RtValueDefinition.ChangeLevel, 
                      dbo.RtValueDefinition.ModeAttribute, dbo.RtValueDefinition.ModeValue, dbo.RtGradeDetail.RecommendedValue, dbo.RtGradeDetail.LowLimit, dbo.RtGradeDetail.HighLimit, 
                      dbo.RtValueType.ValueType
FROM         dbo.RtRecipeFamily INNER JOIN
                      dbo.RtGradeMaster ON dbo.RtRecipeFamily.RecipeFamilyId = dbo.RtGradeMaster.RecipeFamilyId INNER JOIN
                      dbo.RtGradeDetail ON dbo.RtGradeMaster.RecipeFamilyId = dbo.RtGradeDetail.RecipeFamilyId AND dbo.RtGradeMaster.Grade = dbo.RtGradeDetail.Grade AND 
                      dbo.RtGradeMaster.Version = dbo.RtGradeDetail.Version INNER JOIN
                      dbo.RtValueDefinition ON dbo.RtGradeDetail.RecipeFamilyId = dbo.RtValueDefinition.RecipeFamilyId AND dbo.RtGradeDetail.ValueId = dbo.RtValueDefinition.ValueId INNER JOIN
                      dbo.RtValueType ON dbo.RtValueDefinition.ValueTypeId = dbo.RtValueType.ValueTypeId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[35] 4[28] 2[14] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = -222
      End
      Begin Tables = 
         Begin Table = "RtRecipeFamily"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 238
               Right = 216
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtGradeMaster"
            Begin Extent = 
               Top = 11
               Left = 279
               Bottom = 156
               Right = 440
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtGradeDetail"
            Begin Extent = 
               Top = 10
               Left = 511
               Bottom = 193
               Right = 696
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtValueDefinition"
            Begin Extent = 
               Top = 9
               Left = 727
               Bottom = 239
               Right = 905
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtValueType"
            Begin Extent = 
               Top = 158
               Left = 958
               Bottom = 248
               Right = 1118
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 17
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 2070
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
 ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtRecipeView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'        Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtRecipeView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtRecipeView'
GO
/****** Object:  View [dbo].[SfcRecipeDataTimerView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataTimerView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeDataType.JavaClassName, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, 
                      dbo.SfcRecipeDataTimer.StartTime, dbo.SfcRecipeDataTimer.StopTime, dbo.SfcRecipeDataTimer.TimerState, dbo.SfcRecipeDataTimer.CumulativeMinutes, 
                      dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeData ON dbo.SfcStep.StepId = dbo.SfcRecipeData.StepId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcRecipeDataTimer ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataTimer.RecipeDataId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[36] 4[25] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 8
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 150
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 158
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 6
               Left = 445
               Bottom = 191
               Right = 623
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 155
               Left = 684
               Bottom = 272
               Right = 862
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataTimer"
            Begin Extent = 
               Top = 6
               Left = 681
               Bottom = 156
               Right = 841
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 212
               Left = 200
               Bottom = 369
               Right = 416
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      PaneHidden = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataTimerView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N' ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataTimerView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataTimerView'
GO
/****** Object:  View [dbo].[SfcRecipeDataRecipeView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataRecipeView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, dbo.SfcRecipeDataRecipe.PresentationOrder, 
                      dbo.SfcRecipeDataRecipe.StoreTag, dbo.SfcRecipeDataRecipe.CompareTag, dbo.SfcRecipeDataRecipe.ModeAttribute, dbo.SfcRecipeDataRecipe.ModeValue, 
                      dbo.SfcRecipeDataRecipe.ChangeLevel, dbo.SfcRecipeDataRecipe.RecommendedValue, dbo.SfcRecipeDataRecipe.LowLimit, dbo.SfcRecipeDataRecipe.HighLimit, 
                      dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeData ON dbo.SfcStep.StepId = dbo.SfcRecipeData.StepId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcRecipeDataRecipe ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataRecipe.RecipeDataId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[30] 2[11] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[52] 4[22] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 8
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 153
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 148
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 9
               Left = 460
               Bottom = 201
               Right = 638
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 235
               Left = 667
               Bottom = 354
               Right = 845
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataRecipe"
            Begin Extent = 
               Top = 6
               Left = 676
               Bottom = 222
               Right = 861
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 229
               Left = 215
               Bottom = 397
               Right = 431
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      PaneHidden = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begi' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataRecipeView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'n ColumnWidths = 11
         Column = 2070
         Alias = 2445
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataRecipeView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataRecipeView'
GO
/****** Object:  View [dbo].[SfcRecipeDataOutputView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataOutputView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepName, dbo.SfcStep.StepUUID, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeDataType.JavaClassName, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeDataOutput.Tag, 
                      dbo.SfcRecipeData.Units, dbo.SfcValueType.ValueType, dbo.SfcRecipeDataOutputType.OutputType, dbo.SfcRecipeDataOutput.Download, dbo.SfcRecipeDataOutput.DownloadStatus, 
                      dbo.SfcRecipeDataOutput.ErrorCode, dbo.SfcRecipeDataOutput.ErrorText, dbo.SfcRecipeDataOutput.Timing, dbo.SfcRecipeDataOutput.MaxTiming, dbo.SfcRecipeDataOutput.ActualTiming, 
                      dbo.SfcRecipeDataOutput.ActualDateTime, dbo.SfcRecipeDataOutput.PVMonitorActive, dbo.SfcRecipeDataOutput.PVMonitorStatus, dbo.SfcRecipeDataOutput.WriteConfirm, 
                      dbo.SfcRecipeDataOutput.WriteConfirmed, dbo.SfcRecipeDataOutput.OutputValueId, dbo.SfcRecipeDataValue.FloatValue AS OutputFloatValue, 
                      dbo.SfcRecipeDataValue.IntegerValue AS OutputIntegerValue, dbo.SfcRecipeDataValue.StringValue AS OutputStringValue, dbo.SfcRecipeDataValue.BooleanValue AS OutputBooleanValue, 
                      dbo.SfcRecipeDataOutput.TargetValueId, SfcRecipeDataValue_1.FloatValue AS TargetFloatValue, SfcRecipeDataValue_1.IntegerValue AS TargetIntegerValue, 
                      SfcRecipeDataValue_1.StringValue AS TargetStringValue, SfcRecipeDataValue_1.BooleanValue AS TargetBooleanValue, dbo.SfcRecipeDataOutput.PVValueId, 
                      SfcRecipeDataValue_2.FloatValue AS PVFloatValue, SfcRecipeDataValue_2.IntegerValue AS PVIntegerValue, SfcRecipeDataValue_2.StringValue AS PVStringValue, 
                      SfcRecipeDataValue_2.BooleanValue AS PVBooleanValue, dbo.SfcRecipeDataOutput.SetpointStatus, dbo.SfcRecipeDataOutput.ValueTypeId, dbo.SfcRecipeDataOutput.OutputTypeId, 
                      dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcRecipeDataOutput INNER JOIN
                      dbo.SfcRecipeDataOutputType ON dbo.SfcRecipeDataOutput.OutputTypeId = dbo.SfcRecipeDataOutputType.OutputTypeId INNER JOIN
                      dbo.SfcRecipeData ON dbo.SfcRecipeDataOutput.RecipeDataId = dbo.SfcRecipeData.RecipeDataId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcValueType ON dbo.SfcRecipeDataOutput.ValueTypeId = dbo.SfcValueType.ValueTypeId INNER JOIN
                      dbo.SfcStep ON dbo.SfcRecipeData.StepId = dbo.SfcStep.StepId INNER JOIN
                      dbo.SfcChart ON dbo.SfcStep.ChartId = dbo.SfcChart.ChartId INNER JOIN
                      dbo.SfcRecipeDataValue ON dbo.SfcRecipeDataOutput.OutputValueId = dbo.SfcRecipeDataValue.ValueId INNER JOIN
                      dbo.SfcRecipeDataValue AS SfcRecipeDataValue_1 ON dbo.SfcRecipeDataOutput.TargetValueId = SfcRecipeDataValue_1.ValueId INNER JOIN
                      dbo.SfcRecipeDataValue AS SfcRecipeDataValue_2 ON dbo.SfcRecipeDataOutput.PVValueId = SfcRecipeDataValue_2.ValueId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[60] 4[22] 2[18] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = -192
      End
      Begin Tables = 
         Begin Table = "SfcRecipeDataOutput"
            Begin Extent = 
               Top = 11
               Left = 736
               Bottom = 375
               Right = 935
            End
            DisplayFlags = 280
            TopColumn = 1
         End
         Begin Table = "SfcRecipeDataOutputType"
            Begin Extent = 
               Top = 110
               Left = 998
               Bottom = 200
               Right = 1221
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 12
               Left = 479
               Bottom = 206
               Right = 657
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 379
               Left = 725
               Bottom = 501
               Right = 903
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcValueType"
            Begin Extent = 
               Top = 12
               Left = 999
               Bottom = 102
               Right = 1159
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 147
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 1
         End
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 147
               Right =' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataOutputView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N' 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue"
            Begin Extent = 
               Top = 139
               Left = 1330
               Bottom = 301
               Right = 1490
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue_1"
            Begin Extent = 
               Top = 225
               Left = 1157
               Bottom = 358
               Right = 1317
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue_2"
            Begin Extent = 
               Top = 290
               Left = 991
               Bottom = 426
               Right = 1151
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 284
               Left = 209
               Bottom = 445
               Right = 425
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 48
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2745
         Alias = 2220
         Table = 2745
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataOutputView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataOutputView'
GO
/****** Object:  View [dbo].[SfcRecipeDataOutputRampView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataOutputRampView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, dbo.SfcRecipeDataOutput.Tag, 
                      dbo.SfcRecipeDataOutputType.OutputType, dbo.SfcValueType.ValueType, dbo.SfcRecipeDataOutput.Download, dbo.SfcRecipeDataOutput.DownloadStatus, dbo.SfcRecipeDataOutput.ErrorCode, 
                      dbo.SfcRecipeDataOutput.ErrorText, dbo.SfcRecipeDataOutput.Timing, dbo.SfcRecipeDataOutput.MaxTiming, dbo.SfcRecipeDataOutput.ActualTiming, dbo.SfcRecipeDataOutput.ActualDateTime, 
                      dbo.SfcRecipeDataOutput.PVMonitorActive, dbo.SfcRecipeDataOutput.PVMonitorStatus, dbo.SfcRecipeDataOutput.WriteConfirm, dbo.SfcRecipeDataOutput.WriteConfirmed, 
                      dbo.SfcRecipeDataOutputRamp.RampTimeMinutes, dbo.SfcRecipeDataOutputRamp.UpdateFrequencySeconds, dbo.SfcRecipeDataOutput.OutputValueId, 
                      dbo.SfcRecipeDataValue.FloatValue AS OutputFloatValue, dbo.SfcRecipeDataValue.IntegerValue AS OutputIntegerValue, dbo.SfcRecipeDataValue.StringValue AS OutputStringValue, 
                      dbo.SfcRecipeDataValue.BooleanValue AS OutputBooleanValue, dbo.SfcRecipeDataOutput.TargetValueId, SfcRecipeDataValue_1.FloatValue AS TargetFloatValue, 
                      SfcRecipeDataValue_1.IntegerValue AS TargetIntegerValue, SfcRecipeDataValue_1.StringValue AS TargetStringValue, SfcRecipeDataValue_1.BooleanValue AS TargetBooleanValue, 
                      dbo.SfcRecipeDataOutput.PVValueId, SfcRecipeDataValue_2.FloatValue AS PVFloatValue, SfcRecipeDataValue_2.IntegerValue AS PVIntegerValue, 
                      SfcRecipeDataValue_2.StringValue AS PVStringValue, SfcRecipeDataValue_2.BooleanValue AS PVBooleanValue, dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, 
                      dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcRecipeData INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcRecipeDataOutput ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataOutput.RecipeDataId INNER JOIN
                      dbo.SfcRecipeDataOutputRamp ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataOutputRamp.RecipeDataId INNER JOIN
                      dbo.SfcValueType ON dbo.SfcRecipeDataOutput.ValueTypeId = dbo.SfcValueType.ValueTypeId INNER JOIN
                      dbo.SfcRecipeDataOutputType ON dbo.SfcRecipeDataOutput.OutputTypeId = dbo.SfcRecipeDataOutputType.OutputTypeId INNER JOIN
                      dbo.SfcStep ON dbo.SfcRecipeData.StepId = dbo.SfcStep.StepId INNER JOIN
                      dbo.SfcChart ON dbo.SfcStep.ChartId = dbo.SfcChart.ChartId INNER JOIN
                      dbo.SfcRecipeDataValue ON dbo.SfcRecipeDataOutput.OutputValueId = dbo.SfcRecipeDataValue.ValueId INNER JOIN
                      dbo.SfcRecipeDataValue AS SfcRecipeDataValue_1 ON dbo.SfcRecipeDataOutput.TargetValueId = SfcRecipeDataValue_1.ValueId INNER JOIN
                      dbo.SfcRecipeDataValue AS SfcRecipeDataValue_2 ON dbo.SfcRecipeDataOutput.PVValueId = SfcRecipeDataValue_2.ValueId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[63] 4[24] 2[9] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 32
               Left = 404
               Bottom = 228
               Right = 582
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 498
               Left = 612
               Bottom = 614
               Right = 790
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataOutput"
            Begin Extent = 
               Top = 7
               Left = 621
               Bottom = 379
               Right = 811
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataOutputRamp"
            Begin Extent = 
               Top = 382
               Left = 612
               Bottom = 487
               Right = 827
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcValueType"
            Begin Extent = 
               Top = 6
               Left = 854
               Bottom = 96
               Right = 1014
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataOutputType"
            Begin Extent = 
               Top = 102
               Left = 843
               Bottom = 192
               Right = 1058
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 46
               Left = 202
               Bottom = 200
          ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataOutputRampView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'     Right = 362
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 7
               Left = 1
               Bottom = 149
               Right = 172
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue"
            Begin Extent = 
               Top = 160
               Left = 1072
               Bottom = 298
               Right = 1232
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue_1"
            Begin Extent = 
               Top = 301
               Left = 1070
               Bottom = 440
               Right = 1230
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue_2"
            Begin Extent = 
               Top = 459
               Left = 969
               Bottom = 602
               Right = 1129
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 239
               Left = 158
               Bottom = 413
               Right = 374
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 3495
         Alias = 2115
         Table = 2820
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataOutputRampView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataOutputRampView'
GO
/****** Object:  View [dbo].[SfcRecipeDataMatrixView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataMatrixView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcValueType.ValueType, dbo.SfcRecipeDataMatrix.Rows, 
                      dbo.SfcRecipeDataMatrix.Columns, dbo.SfcRecipeDataMatrix.RowIndexKeyId, dbo.SfcRecipeDataKeyMaster.KeyName AS RowIndexKeyName, dbo.SfcRecipeDataMatrix.ColumnIndexKeyId, 
                      SfcRecipeDataKeyMaster_1.KeyName AS ColumnIndexKeyName, dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeData ON dbo.SfcStep.StepId = dbo.SfcRecipeData.StepId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcRecipeDataMatrix ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataMatrix.RecipeDataId INNER JOIN
                      dbo.SfcValueType ON dbo.SfcRecipeDataMatrix.ValueTypeId = dbo.SfcValueType.ValueTypeId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId LEFT OUTER JOIN
                      dbo.SfcRecipeDataKeyMaster ON dbo.SfcRecipeDataMatrix.RowIndexKeyId = dbo.SfcRecipeDataKeyMaster.KeyId LEFT OUTER JOIN
                      dbo.SfcRecipeDataKeyMaster AS SfcRecipeDataKeyMaster_1 ON dbo.SfcRecipeDataMatrix.ColumnIndexKeyId = SfcRecipeDataKeyMaster_1.KeyId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 8
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 111
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 155
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 10
               Left = 443
               Bottom = 201
               Right = 621
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 207
               Left = 652
               Bottom = 328
               Right = 830
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataMatrix"
            Begin Extent = 
               Top = 16
               Left = 668
               Bottom = 185
               Right = 867
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcValueType"
            Begin Extent = 
               Top = 4
               Left = 945
               Bottom = 94
               Right = 1105
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataKeyMaster"
            Begin Extent = 
               Top = 107
               Left = 946
               Bottom = 197
               Right = 1106
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataMatrixView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataKeyMaster_1"
            Begin Extent = 
               Top = 213
               Left = 945
               Bottom = 303
               Right = 1105
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 205
               Left = 196
               Bottom = 380
               Right = 412
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      PaneHidden = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 10
         Width = 284
         Width = 1500
         Width = 2505
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2085
         Alias = 2850
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataMatrixView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataMatrixView'
GO
/****** Object:  View [dbo].[SfcRecipeDataMatrixElementView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataMatrixElementView]
AS
SELECT     TOP (100) PERCENT dbo.SfcRecipeDataMatrixElement.RecipeDataId, dbo.SfcRecipeDataMatrixElement.RowIndex, dbo.SfcRecipeDataMatrixElement.ColumnIndex, 
                      dbo.SfcRecipeDataValue.FloatValue, dbo.SfcRecipeDataValue.IntegerValue, dbo.SfcRecipeDataValue.StringValue, dbo.SfcRecipeDataValue.BooleanValue
FROM         dbo.SfcRecipeDataMatrixElement INNER JOIN
                      dbo.SfcRecipeDataValue ON dbo.SfcRecipeDataMatrixElement.ValueId = dbo.SfcRecipeDataValue.ValueId
ORDER BY dbo.SfcRecipeDataMatrixElement.RecipeDataId, dbo.SfcRecipeDataMatrixElement.RowIndex, dbo.SfcRecipeDataMatrixElement.ColumnIndex
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcRecipeDataMatrixElement"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 137
               Right = 265
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue"
            Begin Extent = 
               Top = 4
               Left = 311
               Bottom = 154
               Right = 511
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataMatrixElementView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataMatrixElementView'
GO
/****** Object:  Table [dbo].[SfcRecipeDataArrayElement]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[SfcRecipeDataArrayElement](
	[RecipeDataId] [int] NOT NULL,
	[ArrayIndex] [int] NOT NULL,
	[ValueId] [int] NULL,
 CONSTRAINT [PK_SfcRecipeDataArrayElement] PRIMARY KEY CLUSTERED 
(
	[RecipeDataId] ASC,
	[ArrayIndex] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[SfcRecipeDataInputView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataInputView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeDataType.JavaClassName, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, 
                      dbo.SfcRecipeDataInput.Tag, dbo.SfcRecipeDataInput.ErrorCode, dbo.SfcRecipeDataInput.ErrorText, dbo.SfcRecipeDataInput.PVMonitorActive, dbo.SfcRecipeDataInput.PVMonitorStatus, 
                      dbo.SfcValueType.ValueType, dbo.SfcRecipeDataInput.PVValueId, dbo.SfcRecipeDataValue.FloatValue AS PVFloatValue, dbo.SfcRecipeDataValue.IntegerValue AS PVIntegerValue, 
                      dbo.SfcRecipeDataValue.StringValue AS PVStringValue, dbo.SfcRecipeDataValue.BooleanValue AS PVBooleanValue, dbo.SfcRecipeDataInput.TargetValueId, 
                      SfcRecipeDataValue_1.FloatValue AS TargetFloatValue, SfcRecipeDataValue_1.IntegerValue AS TargetIntegerValue, SfcRecipeDataValue_1.StringValue AS TargetStringValue, 
                      SfcRecipeDataValue_1.BooleanValue AS TargetBooleanValue, dbo.SfcRecipeDataInput.ValueTypeId, dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, 
                      dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeData ON dbo.SfcStep.StepId = dbo.SfcRecipeData.StepId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcRecipeDataInput ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataInput.RecipeDataId INNER JOIN
                      dbo.SfcRecipeDataValue ON dbo.SfcRecipeDataInput.PVValueId = dbo.SfcRecipeDataValue.ValueId INNER JOIN
                      dbo.SfcRecipeDataValue AS SfcRecipeDataValue_1 ON dbo.SfcRecipeDataInput.TargetValueId = SfcRecipeDataValue_1.ValueId INNER JOIN
                      dbo.SfcValueType ON dbo.SfcRecipeDataInput.ValueTypeId = dbo.SfcValueType.ValueTypeId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[35] 4[26] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1[50] 4[25] 3) )"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1[46] 4) )"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 9
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 157
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 155
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 0
               Left = 463
               Bottom = 186
               Right = 641
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 215
               Left = 676
               Bottom = 346
               Right = 854
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataInput"
            Begin Extent = 
               Top = 6
               Left = 679
               Bottom = 207
               Right = 858
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue"
            Begin Extent = 
               Top = 103
               Left = 941
               Bottom = 241
               Right = 1132
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue_1"
            Begin Extent = 
               Top = 247
               Left = 939
               Bottom = 387
               Right = 113' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataInputView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'1
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcValueType"
            Begin Extent = 
               Top = 5
               Left = 940
               Bottom = 95
               Right = 1100
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 176
               Left = 210
               Bottom = 333
               Right = 426
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
      PaneHidden = 
   End
   Begin DataPane = 
      PaneHidden = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2430
         Alias = 2730
         Table = 2640
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataInputView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataInputView'
GO
/****** Object:  Table [dbo].[SfcDownloadGUI]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcDownloadGUI](
	[WindowId] [int] NOT NULL,
	[State] [varchar](25) NOT NULL,
	[TimerRecipeDataId] [int] NOT NULL,
	[LastUpdated] [datetime] NULL,
	[StartTime] [datetime] NULL,
 CONSTRAINT [PK_SfcDownloadGUI] PRIMARY KEY CLUSTERED 
(
	[WindowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcManualDataEntry]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcManualDataEntry](
	[windowId] [int] NOT NULL,
	[requireAllInputs] [bit] NOT NULL,
	[complete] [bit] NOT NULL,
	[header] [varchar](1024) NULL,
 CONSTRAINT [PK_SfcManualDataEntry] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcInput]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcInput](
	[windowId] [int] NOT NULL,
	[prompt] [varchar](900) NOT NULL,
	[lowLimit] [float] NULL,
	[highLimit] [float] NULL,
	[targetStepId] [int] NULL,
	[keyAndAttribute] [varchar](255) NULL,
	[defaultValue] [varchar](255) NULL,
 CONSTRAINT [PK_SfcInput] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[SfcRecipeDataArrayView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataArrayView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepName, dbo.SfcStep.StepUUID, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, dbo.SfcValueType.ValueType, dbo.SfcRecipeDataArray.ValueTypeId, 
                      dbo.SfcRecipeDataArray.IndexKeyId, dbo.SfcRecipeDataKeyMaster.KeyName, dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcRecipeData INNER JOIN
                      dbo.SfcRecipeDataArray ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataArray.RecipeDataId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcValueType ON dbo.SfcRecipeDataArray.ValueTypeId = dbo.SfcValueType.ValueTypeId INNER JOIN
                      dbo.SfcStep ON dbo.SfcRecipeData.StepId = dbo.SfcStep.StepId INNER JOIN
                      dbo.SfcChart ON dbo.SfcStep.ChartId = dbo.SfcChart.ChartId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId LEFT OUTER JOIN
                      dbo.SfcRecipeDataKeyMaster ON dbo.SfcRecipeDataArray.IndexKeyId = dbo.SfcRecipeDataKeyMaster.KeyId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[35] 4[29] 2[15] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 8
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 6
               Left = 437
               Bottom = 185
               Right = 615
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataArray"
            Begin Extent = 
               Top = 7
               Left = 664
               Bottom = 126
               Right = 845
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 131
               Left = 664
               Bottom = 250
               Right = 842
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcValueType"
            Begin Extent = 
               Top = 8
               Left = 913
               Bottom = 98
               Right = 1073
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 153
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 136
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataKeyMaster"
            Begin Extent = 
               Top = 127
               Left = 907
               Bottom = 217
               Right = 1119
   ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataArrayView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'         End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 196
               Left = 191
               Bottom = 374
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      PaneHidden = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 17
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataArrayView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataArrayView'
GO
/****** Object:  Table [dbo].[SfcTimeDelayNotification]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcTimeDelayNotification](
	[windowId] [int] NOT NULL,
	[message] [varchar](900) NOT NULL,
	[endTime] [datetime] NOT NULL,
 CONSTRAINT [PK_SfcTimeDelayNotification] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcReviewData]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcReviewData](
	[windowId] [int] NOT NULL,
	[showAdvice] [bit] NOT NULL,
	[targetStepUUID] [varchar](255) NULL,
	[responseKey] [varchar](255) NULL,
	[primaryTabLabel] [varchar](100) NULL,
	[secondaryTabLabel] [varchar](100) NULL,
 CONSTRAINT [PK_SfcManualDataEntryTable] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[SfcRecipeDataSQCView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataSQCView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeData.RecipeDataKey, 
                      dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Units, dbo.SfcRecipeDataSQC.LowLimit, dbo.SfcRecipeDataSQC.TargetValue, dbo.SfcRecipeDataSQC.HighLimit, 
                      dbo.SfcRecipeDataType.RecipeDataType, dbo.SfcRecipeDataType.JavaClassName, dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, 
                      dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcChart INNER JOIN
                      dbo.SfcStep ON dbo.SfcChart.ChartId = dbo.SfcStep.ChartId INNER JOIN
                      dbo.SfcRecipeData ON dbo.SfcStep.StepId = dbo.SfcRecipeData.StepId INNER JOIN
                      dbo.SfcRecipeDataSQC ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataSQC.RecipeDataId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[51] 4[23] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 162
               Right = 209
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 6
               Left = 247
               Bottom = 161
               Right = 407
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 6
               Left = 445
               Bottom = 203
               Right = 629
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataSQC"
            Begin Extent = 
               Top = 6
               Left = 667
               Bottom = 126
               Right = 853
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 136
               Left = 661
               Bottom = 241
               Right = 839
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 224
               Left = 181
               Bottom = 401
               Right = 397
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width =' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataSQCView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N' 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1980
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataSQCView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataSQCView'
GO
/****** Object:  View [dbo].[SfcRecipeDataSimpleValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataSimpleValueView]
AS
SELECT     dbo.SfcChart.ChartId, dbo.SfcChart.ChartPath, dbo.SfcStep.StepId, dbo.SfcStep.StepUUID, dbo.SfcStep.StepName, dbo.SfcRecipeData.RecipeDataId, dbo.SfcRecipeDataType.RecipeDataType, 
                      dbo.SfcRecipeDataType.JavaClassName, dbo.SfcRecipeData.RecipeDataKey, dbo.SfcRecipeData.Description, dbo.SfcRecipeData.Label, dbo.SfcRecipeData.Units, dbo.SfcValueType.ValueType, 
                      dbo.SfcRecipeDataSimpleValue.ValueId, dbo.SfcRecipeDataValue.FloatValue, dbo.SfcRecipeDataValue.IntegerValue, dbo.SfcRecipeDataValue.StringValue, dbo.SfcRecipeDataValue.BooleanValue, 
                      dbo.SfcRecipeDataSimpleValue.ValueTypeId, dbo.SfcRecipeData.RecipeDataFolderId AS FolderId, dbo.SfcRecipeDataFolder.RecipeDataKey AS FolderKey
FROM         dbo.SfcRecipeData INNER JOIN
                      dbo.SfcRecipeDataSimpleValue ON dbo.SfcRecipeData.RecipeDataId = dbo.SfcRecipeDataSimpleValue.RecipeDataId INNER JOIN
                      dbo.SfcRecipeDataType ON dbo.SfcRecipeData.RecipeDataTypeId = dbo.SfcRecipeDataType.RecipeDataTypeId INNER JOIN
                      dbo.SfcValueType ON dbo.SfcRecipeDataSimpleValue.ValueTypeId = dbo.SfcValueType.ValueTypeId INNER JOIN
                      dbo.SfcStep ON dbo.SfcRecipeData.StepId = dbo.SfcStep.StepId INNER JOIN
                      dbo.SfcChart ON dbo.SfcStep.ChartId = dbo.SfcChart.ChartId INNER JOIN
                      dbo.SfcRecipeDataValue ON dbo.SfcRecipeDataSimpleValue.ValueId = dbo.SfcRecipeDataValue.ValueId LEFT OUTER JOIN
                      dbo.SfcRecipeDataFolder ON dbo.SfcRecipeData.RecipeDataFolderId = dbo.SfcRecipeDataFolder.RecipeDataFolderId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = -96
      End
      Begin Tables = 
         Begin Table = "SfcRecipeData"
            Begin Extent = 
               Top = 7
               Left = 483
               Bottom = 205
               Right = 661
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataSimpleValue"
            Begin Extent = 
               Top = 7
               Left = 723
               Bottom = 169
               Right = 956
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataType"
            Begin Extent = 
               Top = 179
               Left = 721
               Bottom = 288
               Right = 899
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcValueType"
            Begin Extent = 
               Top = 23
               Left = 1001
               Bottom = 113
               Right = 1161
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcStep"
            Begin Extent = 
               Top = 15
               Left = 232
               Bottom = 167
               Right = 392
            End
            DisplayFlags = 280
            TopColumn = 1
         End
         Begin Table = "SfcChart"
            Begin Extent = 
               Top = 16
               Left = 11
               Bottom = 152
               Right = 182
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue"
            Begin Extent = 
               Top = 121
               Left = 992
               Bottom = 270
               Right = ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataSimpleValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'1152
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataFolder"
            Begin Extent = 
               Top = 197
               Left = 213
               Bottom = 317
               Right = 429
            End
            DisplayFlags = 280
            TopColumn = 2
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 22
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2715
         Alias = 2085
         Table = 2955
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataSimpleValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataSimpleValueView'
GO
/****** Object:  Table [dbo].[SfcSelectInput]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcSelectInput](
	[windowId] [int] NOT NULL,
	[prompt] [varchar](900) NOT NULL,
	[choicesStepId] [int] NOT NULL,
	[choicesKey] [varchar](500) NOT NULL,
	[targetStepId] [int] NULL,
	[keyAndAttribute] [varchar](255) NULL,
 CONSTRAINT [PK_SfcSelectInput] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcReviewFlows]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcReviewFlows](
	[windowId] [int] NOT NULL,
	[heading1] [varchar](900) NOT NULL,
	[heading2] [varchar](900) NOT NULL,
	[heading3] [varchar](900) NOT NULL,
	[targetStepUUID] [varchar](255) NOT NULL,
	[responseKey] [varchar](255) NOT NULL,
	[primaryTabLabel] [varchar](100) NULL,
	[secondaryTabLabel] [varchar](100) NULL,
 CONSTRAINT [PK_SfcReviewFlows] PRIMARY KEY CLUSTERED 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcReviewFlowsTable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcReviewFlowsTable](
	[windowId] [int] NOT NULL,
	[rowNum] [int] NOT NULL,
	[configKey] [varchar](150) NOT NULL,
	[advice] [varchar](900) NOT NULL,
	[units] [varchar](900) NOT NULL,
	[prompt] [varchar](900) NOT NULL,
	[data1] [varchar](150) NOT NULL,
	[data2] [varchar](150) NOT NULL,
	[data3] [varchar](150) NOT NULL,
	[isPrimary] [bit] NOT NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE NONCLUSTERED INDEX [idx_SfcReviewFlowsTable] ON [dbo].[SfcReviewFlowsTable] 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[SfcReviewDataTable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcReviewDataTable](
	[windowId] [int] NOT NULL,
	[rowNum] [int] NOT NULL,
	[configKey] [varchar](150) NULL,
	[prompt] [varchar](150) NULL,
	[value] [varchar](150) NULL,
	[units] [varchar](20) NULL,
	[isPrimary] [bit] NOT NULL,
	[advice] [varchar](150) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE NONCLUSTERED INDEX [idx_SfcReviewDataTable] ON [dbo].[SfcReviewDataTable] 
(
	[windowId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[SfcRecipeDataArrayElementView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[SfcRecipeDataArrayElementView]
AS
SELECT     dbo.SfcRecipeDataArrayElement.RecipeDataId, dbo.SfcRecipeDataArrayElement.ArrayIndex, dbo.SfcRecipeDataArrayElement.ValueId, dbo.SfcRecipeDataValue.FloatValue, 
                      dbo.SfcRecipeDataValue.IntegerValue, dbo.SfcRecipeDataValue.StringValue, dbo.SfcRecipeDataValue.BooleanValue
FROM         dbo.SfcRecipeDataArrayElement INNER JOIN
                      dbo.SfcRecipeDataValue ON dbo.SfcRecipeDataArrayElement.ValueId = dbo.SfcRecipeDataValue.ValueId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "SfcRecipeDataArrayElement"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 111
               Right = 266
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "SfcRecipeDataValue"
            Begin Extent = 
               Top = 34
               Left = 324
               Bottom = 198
               Right = 517
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataArrayElementView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'SfcRecipeDataArrayElementView'
GO
/****** Object:  Table [dbo].[SfcManualDataEntryTable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcManualDataEntryTable](
	[windowId] [int] NOT NULL,
	[rowNum] [int] NOT NULL,
	[description] [varchar](900) NOT NULL,
	[value] [varchar](900) NOT NULL,
	[units] [varchar](900) NOT NULL,
	[dataKey] [varchar](900) NOT NULL,
	[destination] [varchar](900) NOT NULL,
	[targetStepId] [int] NULL,
	[type] [varchar](900) NOT NULL,
	[recipeUnits] [varchar](900) NOT NULL,
	[lowLimit] [float] NULL,
	[highLimit] [float] NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  Table [dbo].[SfcDownloadGUITable]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[SfcDownloadGUITable](
	[windowId] [int] NOT NULL,
	[RecipeDataId] [int] NULL,
	[RecipeDataType] [varchar](50) NULL,
	[LabelAttribute] [varchar](50) NULL,
	[RawTiming] [float] NULL,
	[Timing] [float] NULL,
	[DcsTagId] [varchar](900) NULL,
	[SetPoint] [varchar](50) NULL,
	[Description] [varchar](900) NULL,
	[StepTimestamp] [varchar](900) NULL,
	[PV] [varchar](50) NULL,
	[DownloadStatus] [varchar](900) NULL,
	[PVMonitorStatus] [varchar](900) NULL,
	[SetpointStatus] [varchar](900) NULL
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[RtSQCLimitView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[RtSQCLimitView]
AS
SELECT     dbo.RtRecipeFamily.RecipeFamilyName, dbo.RtSQCParameter.Parameter, dbo.RtSQCLimit.Grade, dbo.RtSQCLimit.UpperLimit, dbo.RtSQCLimit.LowerLimit
FROM         dbo.RtSQCParameter INNER JOIN
                      dbo.RtSQCLimit ON dbo.RtSQCParameter.ParameterId = dbo.RtSQCLimit.ParameterId INNER JOIN
                      dbo.RtRecipeFamily ON dbo.RtSQCParameter.RecipeFamilyId = dbo.RtRecipeFamily.RecipeFamilyId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "RtSQCParameter"
            Begin Extent = 
               Top = 32
               Left = 291
               Bottom = 137
               Right = 452
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtSQCLimit"
            Begin Extent = 
               Top = 33
               Left = 535
               Bottom = 153
               Right = 695
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtRecipeFamily"
            Begin Extent = 
               Top = 47
               Left = 38
               Bottom = 278
               Right = 216
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtSQCLimitView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtSQCLimitView'
GO
/****** Object:  Table [dbo].[LtRelatedData]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[LtRelatedData](
	[RelatedDataId] [int] IDENTITY(1,1) NOT NULL,
	[DerivedValueId] [int] NOT NULL,
	[RelatedValueId] [int] NOT NULL,
 CONSTRAINT [PK_LtRelatedData] PRIMARY KEY CLUSTERED 
(
	[RelatedDataId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[LtPHDValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtPHDValueView]
AS
SELECT     dbo.LtValue.ValueId, dbo.LtValue.ValueName, dbo.LtValue.Description, dbo.LtValue.DisplayDecimals, dbo.LtValue.LastHistoryId, dbo.LtPHDValue.ItemId, dbo.LtHDAInterface.InterfaceName, 
                      dbo.TkPost.Post, dbo.TkUnit.UnitName, dbo.LtValue.ValidationProcedure, dbo.LtHistory.SampleTime, dbo.LtPHDValue.AllowManualEntry
FROM         dbo.LtValue INNER JOIN
                      dbo.LtPHDValue ON dbo.LtValue.ValueId = dbo.LtPHDValue.ValueId INNER JOIN
                      dbo.LtHDAInterface ON dbo.LtPHDValue.InterfaceId = dbo.LtHDAInterface.InterfaceId INNER JOIN
                      dbo.TkUnit ON dbo.LtValue.UnitId = dbo.TkUnit.UnitId INNER JOIN
                      dbo.TkPost ON dbo.TkUnit.PostId = dbo.TkPost.PostId LEFT OUTER JOIN
                      dbo.LtHistory ON dbo.LtValue.LastHistoryId = dbo.LtHistory.HistoryId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 19
               Left = 417
               Bottom = 185
               Right = 601
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtPHDValue"
            Begin Extent = 
               Top = 6
               Left = 660
               Bottom = 143
               Right = 820
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHDAInterface"
            Begin Extent = 
               Top = 64
               Left = 889
               Bottom = 154
               Right = 1050
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 22
               Left = 224
               Bottom = 178
               Right = 384
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 23
               Left = 18
               Bottom = 164
               Right = 191
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHistory"
            Begin Extent = 
               Top = 145
               Left = 660
               Bottom = 298
               Right = 820
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
    ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtPHDValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'     Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtPHDValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtPHDValueView'
GO
/****** Object:  View [dbo].[RtGainView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[RtGainView]
AS
SELECT     dbo.RtRecipeFamily.RecipeFamilyId, dbo.RtRecipeFamily.RecipeFamilyName, dbo.RtGain.ParameterId, dbo.RtGain.Parameter, dbo.RtGainGrade.Grade, dbo.RtGainGrade.Gain
FROM         dbo.RtRecipeFamily INNER JOIN
                      dbo.RtGain ON dbo.RtRecipeFamily.RecipeFamilyId = dbo.RtGain.RecipeFamilyId INNER JOIN
                      dbo.RtGainGrade ON dbo.RtGain.ParameterId = dbo.RtGainGrade.ParameterId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "RtRecipeFamily"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 244
               Right = 216
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtGainGrade"
            Begin Extent = 
               Top = 6
               Left = 697
               Bottom = 125
               Right = 857
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "RtGain"
            Begin Extent = 
               Top = 6
               Left = 254
               Bottom = 130
               Right = 415
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtGainView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'RtGainView'
GO
/****** Object:  View [dbo].[LtValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtValueView]
AS
SELECT     dbo.TkUnit.UnitName, dbo.LtValue.ValueName, dbo.LtValue.ValueId, dbo.LtHistory.SampleTime, dbo.LtHistory.RawValue, dbo.LtHistory.ReportTime, dbo.LtHistory.Grade
FROM         dbo.TkUnit INNER JOIN
                      dbo.LtValue ON dbo.TkUnit.UnitId = dbo.LtValue.UnitId LEFT OUTER JOIN
                      dbo.LtHistory ON dbo.LtValue.ValueId = dbo.LtHistory.ValueId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[21] 2[14] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "LtHistory"
            Begin Extent = 
               Top = 27
               Left = 504
               Bottom = 218
               Right = 664
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 19
               Left = 245
               Bottom = 216
               Right = 429
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 125
               Right = 198
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1245
         Width = 2520
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtValueView'
GO
/****** Object:  View [dbo].[LtLocalValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtLocalValueView]
AS
SELECT     dbo.LtValue.ValueId, dbo.LtValue.ValueName, dbo.TkUnit.UnitName, dbo.LtValue.DisplayDecimals, dbo.LtValue.Description, dbo.LtValue.ValidationProcedure, dbo.LtHDAInterface.InterfaceName, 
                      dbo.LtLocalValue.ItemId
FROM         dbo.LtValue INNER JOIN
                      dbo.LtLocalValue ON dbo.LtValue.ValueId = dbo.LtLocalValue.ValueId INNER JOIN
                      dbo.LtHDAInterface ON dbo.LtLocalValue.InterfaceId = dbo.LtHDAInterface.InterfaceId INNER JOIN
                      dbo.TkUnit ON dbo.LtValue.UnitId = dbo.TkUnit.UnitId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 180
               Right = 222
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtLocalValue"
            Begin Extent = 
               Top = 6
               Left = 260
               Bottom = 184
               Right = 420
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHDAInterface"
            Begin Extent = 
               Top = 6
               Left = 458
               Bottom = 96
               Right = 619
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 186
               Left = 259
               Bottom = 306
               Right = 419
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
    ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtLocalValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'  End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtLocalValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtLocalValueView'
GO
/****** Object:  View [dbo].[LtLimitView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtLimitView]
AS
SELECT     dbo.LtLimit.LimitId, dbo.LtValue.ValueId, dbo.LtValue.ValueName, dbo.LtValue.Description, dbo.TkUnit.UnitName, dbo.TkPost.Post, dbo.LtLimit.UpperValidityLimit, dbo.LtLimit.LowerValidityLimit, 
                      dbo.LtLimit.UpperSQCLimit, dbo.LtLimit.LowerSQCLimit, dbo.LtLimit.UpperReleaseLimit, dbo.LtLimit.LowerReleaseLimit, dbo.LtLimit.Target, dbo.LtLimit.StandardDeviation, 
                      dbo.LtValue.ValidationProcedure, dbo.LtLimit.RecipeParameterName, dbo.LtLimit.OPCUpperItemId, dbo.LtLimit.OPCLowerItemId, dbo.Lookup.LookupName AS LimitType, 
                      Lookup_1.LookupName AS LimitSource, dbo.LtOPCInterface.InterfaceName
FROM         dbo.LtValue INNER JOIN
                      dbo.TkUnit ON dbo.LtValue.UnitId = dbo.TkUnit.UnitId INNER JOIN
                      dbo.TkPost ON dbo.TkUnit.PostId = dbo.TkPost.PostId INNER JOIN
                      dbo.LtLimit ON dbo.LtValue.ValueId = dbo.LtLimit.ValueId INNER JOIN
                      dbo.Lookup ON dbo.LtLimit.LimitTypeId = dbo.Lookup.LookupId INNER JOIN
                      dbo.Lookup AS Lookup_1 ON dbo.LtLimit.LimitSourceId = Lookup_1.LookupId LEFT OUTER JOIN
                      dbo.LtOPCInterface ON dbo.LtLimit.OPCInterfaceId = dbo.LtOPCInterface.InterfaceId
WHERE     (dbo.Lookup.LookupTypeCode = 'RtLimitType') AND (Lookup_1.LookupTypeCode = 'RtLimitSource')
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 16
               Left = 437
               Bottom = 193
               Right = 621
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 15
               Left = 230
               Bottom = 185
               Right = 390
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 18
               Left = 17
               Bottom = 171
               Right = 190
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtLimit"
            Begin Extent = 
               Top = 20
               Left = 677
               Bottom = 326
               Right = 875
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "Lookup"
            Begin Extent = 
               Top = 24
               Left = 1167
               Bottom = 144
               Right = 1343
            End
            DisplayFlags = 280
            TopColumn = 1
         End
         Begin Table = "Lookup_1"
            Begin Extent = 
               Top = 153
               Left = 1179
               Bottom = 273
               Right = 1355
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtOPCInterface"
            Begin Extent = 
               Top = 245
               Left = 960
               Bottom = 335
               Right = 1121
            End
            DisplayF' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtLimitView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'lags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 22
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtLimitView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtLimitView'
GO
/****** Object:  View [dbo].[LtLastValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtLastValueView]
AS
SELECT     dbo.LtValue.ValueId, dbo.LtHistory.HistoryId, dbo.LtValue.ValueName, dbo.LtHistory.RawValue, dbo.LtHistory.SampleTime, dbo.LtHistory.ReportTime
FROM         dbo.LtHistory RIGHT OUTER JOIN
                      dbo.LtValue ON dbo.LtHistory.HistoryId = dbo.LtValue.LastHistoryId
GO
/****** Object:  View [dbo].[LtSelectorView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtSelectorView]
AS
SELECT     dbo.LtValue.ValueId, dbo.LtValue.ValueName, dbo.LtValue.Description, dbo.LtHistory.SampleTime, LtValue_1.ValueId AS SourceValueId, LtValue_1.ValueName AS SourceValueName, 
                      dbo.LtValue.LastHistoryId
FROM         dbo.LtValue INNER JOIN
                      dbo.LtSelector ON dbo.LtValue.ValueId = dbo.LtSelector.ValueId LEFT OUTER JOIN
                      dbo.LtValue AS LtValue_1 ON dbo.LtSelector.sourceValueId = LtValue_1.ValueId LEFT OUTER JOIN
                      dbo.LtHistory ON dbo.LtValue.LastHistoryId = dbo.LtHistory.HistoryId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 22
               Left = 227
               Bottom = 201
               Right = 411
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtSelector"
            Begin Extent = 
               Top = 18
               Left = 463
               Bottom = 170
               Right = 628
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtValue_1"
            Begin Extent = 
               Top = 77
               Left = 735
               Bottom = 251
               Right = 919
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHistory"
            Begin Extent = 
               Top = 177
               Left = 456
               Bottom = 337
               Right = 616
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 1875
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtSelectorView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'      Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtSelectorView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtSelectorView'
GO
/****** Object:  View [dbo].[LtDerivedValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtDerivedValueView]
AS
SELECT     dbo.LtValue.ValueName, dbo.LtValue.ValueId, dbo.LtDerivedValue.DerivedValueId, dbo.TkUnit.UnitName, LtValue_1.ValueName AS TriggerValueName, LtValue_1.ValueId AS TriggerValueId, 
                      TkUnit_1.UnitName AS TriggerUnitName, dbo.LtDerivedValue.Callback, dbo.LtDerivedValue.SampleTimeTolerance, dbo.LtDerivedValue.NewSampleWaitTime, 
                      dbo.LtHistory.RawValue AS TriggerRawValue, dbo.LtHistory.SampleTime AS TriggerSampleTime, dbo.LtHistory.ReportTime AS TriggerReportTime, dbo.LtHDAInterface.InterfaceName, 
                      dbo.LtDerivedValue.ResultItemId
FROM         dbo.LtValue INNER JOIN
                      dbo.LtDerivedValue ON dbo.LtValue.ValueId = dbo.LtDerivedValue.ValueId INNER JOIN
                      dbo.LtValue AS LtValue_1 ON dbo.LtDerivedValue.TriggerValueId = LtValue_1.ValueId INNER JOIN
                      dbo.TkUnit ON dbo.LtValue.UnitId = dbo.TkUnit.UnitId INNER JOIN
                      dbo.TkUnit AS TkUnit_1 ON LtValue_1.UnitId = TkUnit_1.UnitId INNER JOIN
                      dbo.LtHDAInterface ON dbo.LtDerivedValue.ResultInterfaceId = dbo.LtHDAInterface.InterfaceId LEFT OUTER JOIN
                      dbo.LtHistory ON LtValue_1.LastHistoryId = dbo.LtHistory.HistoryId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[33] 2[9] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 67
               Left = 246
               Bottom = 244
               Right = 430
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtDerivedValue"
            Begin Extent = 
               Top = 52
               Left = 515
               Bottom = 244
               Right = 707
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtValue_1"
            Begin Extent = 
               Top = 274
               Left = 252
               Bottom = 458
               Right = 436
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 98
               Left = 30
               Bottom = 256
               Right = 190
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit_1"
            Begin Extent = 
               Top = 303
               Left = 25
               Bottom = 463
               Right = 185
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHDAInterface"
            Begin Extent = 
               Top = 158
               Left = 777
               Bottom = 311
               Right = 938
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHistory"
            Begin Extent = 
               Top = 364
               Left = 516
               Bottom = 484
               Right = 676
            End
            ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtDerivedValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2295
         Alias = 900
         Table = 2640
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtDerivedValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtDerivedValueView'
GO
/****** Object:  Table [dbo].[DtFinalDiagnosis]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtFinalDiagnosis](
	[FinalDiagnosisId] [int] IDENTITY(1,1) NOT NULL,
	[FinalDiagnosisName] [varchar](250) NOT NULL,
	[FinalDiagnosisLabel] [varchar](250) NULL,
	[FamilyId] [int] NOT NULL,
	[FinalDiagnosisPriority] [float] NOT NULL,
	[CalculationMethod] [varchar](1000) NULL,
	[Constant] [bit] NULL,
	[PostTextRecommendation] [bit] NOT NULL,
	[PostProcessingCallback] [varchar](1000) NULL,
	[RefreshRate] [int] NOT NULL,
	[Comment] [varchar](1000) NULL,
	[TextRecommendation] [varchar](1000) NULL,
	[State] [bit] NULL,
	[Active] [bit] NOT NULL,
	[Explanation] [varchar](1000) NULL,
	[TrapInsignificantRecommendations] [bit] NULL,
	[LastRecommendationTime] [datetime] NULL,
	[TimeOfMostRecentRecommendationImplementation] [datetime] NOT NULL,
	[FinalDiagnosisUUID] [varchar](100) NULL,
	[DiagramUUID] [varchar](100) NULL,
	[ManualMoveAllowed] [bit] NULL,
	[ManualMove] [float] NULL,
	[ShowExplanationWithRecommendation] [bit] NOT NULL,
 CONSTRAINT [PK_DtFinalDiagnosis] PRIMARY KEY CLUSTERED 
(
	[FinalDiagnosisId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_DtFinalDiagnosis] ON [dbo].[DtFinalDiagnosis] 
(
	[FinalDiagnosisName] ASC,
	[FamilyId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DtQuantOutputRamp]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DtQuantOutputRamp](
	[QuantOutputId] [int] NOT NULL,
	[Ramp] [float] NULL,
	[RampTypeId] [int] NULL,
 CONSTRAINT [PK_DtQuantOutputRamp] PRIMARY KEY CLUSTERED 
(
	[QuantOutputId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  View [dbo].[DtQuantOutputDefinitionView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[DtQuantOutputDefinitionView]
AS
SELECT     TOP (100) PERCENT dbo.DtApplication.ApplicationName, dbo.DtQuantOutput.QuantOutputName, dbo.DtQuantOutput.QuantOutputId, dbo.DtQuantOutput.TagPath, 
                      dbo.DtQuantOutput.MostNegativeIncrement, dbo.DtQuantOutput.MostPositiveIncrement, dbo.DtQuantOutput.IgnoreMinimumIncrement, dbo.DtQuantOutput.MinimumIncrement, 
                      dbo.DtQuantOutput.SetpointHighLimit, dbo.DtQuantOutput.SetpointLowLimit, dbo.DtQuantOutput.IncrementalOutput
FROM         dbo.DtApplication INNER JOIN
                      dbo.DtQuantOutput ON dbo.DtApplication.ApplicationId = dbo.DtQuantOutput.ApplicationId
ORDER BY dbo.DtApplication.ApplicationName, dbo.DtQuantOutput.QuantOutputName
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "DtApplication"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 241
               Right = 229
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtQuantOutput"
            Begin Extent = 
               Top = 6
               Left = 267
               Bottom = 273
               Right = 587
            End
            DisplayFlags = 280
            TopColumn = 12
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtQuantOutputDefinitionView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtQuantOutputDefinitionView'
GO
/****** Object:  Table [dbo].[DtSQCDiagnosis]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtSQCDiagnosis](
	[SQCDiagnosisId] [int] IDENTITY(1,1) NOT NULL,
	[SQCDiagnosisName] [varchar](50) NOT NULL,
	[SQCDiagnosisLabel] [varchar](50) NULL,
	[Status] [varchar](50) NOT NULL,
	[FamilyId] [int] NOT NULL,
	[SQCDiagnosisUUID] [varchar](100) NULL,
	[DiagramUUID] [varchar](100) NULL,
	[LastResetTime] [datetime] NULL,
 CONSTRAINT [PK_DtSQCDiagnosis] PRIMARY KEY CLUSTERED 
(
	[SQCDiagnosisId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
CREATE UNIQUE NONCLUSTERED INDEX [UK_DtSQCDiagnosis] ON [dbo].[DtSQCDiagnosis] 
(
	[SQCDiagnosisName] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, DROP_EXISTING = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
GO
/****** Object:  View [dbo].[LtDCSValueView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[LtDCSValueView]
AS
SELECT     dbo.TkPost.Post, dbo.TkUnit.UnitName, dbo.LtValue.ValueId, dbo.LtValue.ValueName, dbo.LtValue.DisplayDecimals, dbo.LtValue.Description, dbo.LtValue.ValidationProcedure, 
                      dbo.LtOPCInterface.InterfaceName, dbo.LtDCSValue.ItemId, dbo.LtValue.LastHistoryId, dbo.LtHistory.RawValue, dbo.LtHistory.SampleTime, dbo.LtDCSValue.AllowManualEntry
FROM         dbo.TkPost INNER JOIN
                      dbo.TkUnit ON dbo.TkPost.PostId = dbo.TkUnit.PostId INNER JOIN
                      dbo.LtValue ON dbo.TkUnit.UnitId = dbo.LtValue.UnitId INNER JOIN
                      dbo.LtDCSValue ON dbo.LtValue.ValueId = dbo.LtDCSValue.ValueId INNER JOIN
                      dbo.LtOPCInterface ON dbo.LtDCSValue.InterfaceId = dbo.LtOPCInterface.InterfaceId INNER JOIN
                      dbo.LtHistory ON dbo.LtValue.LastHistoryId = dbo.LtHistory.HistoryId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[44] 4[17] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "TkPost"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 147
               Right = 211
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "TkUnit"
            Begin Extent = 
               Top = 6
               Left = 249
               Bottom = 164
               Right = 409
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtValue"
            Begin Extent = 
               Top = 6
               Left = 447
               Bottom = 183
               Right = 631
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtDCSValue"
            Begin Extent = 
               Top = 6
               Left = 669
               Bottom = 162
               Right = 910
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtOPCInterface"
            Begin Extent = 
               Top = 6
               Left = 948
               Bottom = 96
               Right = 1109
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "LtHistory"
            Begin Extent = 
               Top = 165
               Left = 666
               Bottom = 318
               Right = 826
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1440
         ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtDCSValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtDCSValueView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'LtDCSValueView'
GO
/****** Object:  Trigger [FinalDiagnosisUpdateTrigger]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TRIGGER [dbo].[FinalDiagnosisUpdateTrigger] 
   ON  [dbo].[DtFinalDiagnosis] 
   FOR UPDATE
AS 
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

	IF UPDATE(Active) or UPDATE(State)
	BEGIN
		insert into DtFinalDiagnosisLog (Timestamp, FinalDiagnosisId, State, Active) 
		select getdate(), FinalDiagnosisId, State, Active from inserted
    END

END
GO
/****** Object:  View [dbo].[DtSQCDiagnosisView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[DtSQCDiagnosisView]
AS
SELECT     dbo.DtApplication.ApplicationName, dbo.DtFamily.FamilyName, dbo.DtSQCDiagnosis.SQCDiagnosisName, dbo.DtSQCDiagnosis.SQCDiagnosisLabel, dbo.DtSQCDiagnosis.Status, 
                      dbo.DtSQCDiagnosis.SQCDiagnosisUUID, dbo.DtSQCDiagnosis.DiagramUUID, dbo.DtSQCDiagnosis.LastResetTime
FROM         dbo.DtApplication INNER JOIN
                      dbo.DtFamily ON dbo.DtApplication.ApplicationId = dbo.DtFamily.ApplicationId RIGHT OUTER JOIN
                      dbo.DtSQCDiagnosis ON dbo.DtFamily.FamilyId = dbo.DtSQCDiagnosis.FamilyId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "DtApplication"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 181
               Right = 229
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtFamily"
            Begin Extent = 
               Top = 6
               Left = 267
               Bottom = 161
               Right = 427
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtSQCDiagnosis"
            Begin Extent = 
               Top = 6
               Left = 465
               Bottom = 177
               Right = 647
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2805
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtSQCDiagnosisView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtSQCDiagnosisView'
GO
/****** Object:  View [dbo].[DtFinalDiagnosisView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[DtFinalDiagnosisView]
AS
SELECT     dbo.DtApplication.ApplicationName, dbo.DtFamily.FamilyId, dbo.DtFamily.FamilyName, dbo.DtFamily.FamilyPriority, dbo.DtFinalDiagnosis.FinalDiagnosisId, 
                      dbo.DtFinalDiagnosis.FinalDiagnosisName, dbo.DtFinalDiagnosis.FinalDiagnosisLabel, dbo.DtFinalDiagnosis.FinalDiagnosisPriority, dbo.DtFinalDiagnosis.CalculationMethod, 
                      dbo.DtFinalDiagnosis.PostProcessingCallback, dbo.DtFinalDiagnosis.PostTextRecommendation, dbo.DtFinalDiagnosis.RefreshRate, dbo.DtFinalDiagnosis.TextRecommendation, 
                      dbo.DtFinalDiagnosis.Active, dbo.DtFinalDiagnosis.Explanation, dbo.DtFinalDiagnosis.TrapInsignificantRecommendations, dbo.DtFinalDiagnosis.LastRecommendationTime, 
                      dbo.DtFinalDiagnosis.TimeOfMostRecentRecommendationImplementation, dbo.DtFinalDiagnosis.Constant, dbo.DtFinalDiagnosis.DiagramUUID, dbo.DtFinalDiagnosis.FinalDiagnosisUUID, 
                      dbo.DtFinalDiagnosis.ManualMove, dbo.DtFinalDiagnosis.ManualMoveAllowed, dbo.DtFinalDiagnosis.Comment
FROM         dbo.DtApplication INNER JOIN
                      dbo.DtFamily ON dbo.DtApplication.ApplicationId = dbo.DtFamily.ApplicationId INNER JOIN
                      dbo.DtFinalDiagnosis ON dbo.DtFamily.FamilyId = dbo.DtFinalDiagnosis.FamilyId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[43] 4[18] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[50] 4[25] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 8
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "DtApplication"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 240
               Right = 229
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtFamily"
            Begin Extent = 
               Top = 6
               Left = 267
               Bottom = 167
               Right = 427
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtFinalDiagnosis"
            Begin Extent = 
               Top = 6
               Left = 465
               Bottom = 380
               Right = 801
            End
            DisplayFlags = 280
            TopColumn = 2
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      PaneHidden = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 2190
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 4320
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtFinalDiagnosisView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtFinalDiagnosisView'
GO
/****** Object:  View [dbo].[DtFinalDiagnosisLogView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[DtFinalDiagnosisLogView]
AS
SELECT     dbo.DtFinalDiagnosisLog.LogId, dbo.DtFinalDiagnosisLog.Timestamp, dbo.DtApplication.ApplicationName, dbo.DtFamily.FamilyName, dbo.DtFamily.FamilyPriority, 
                      dbo.DtFinalDiagnosis.FinalDiagnosisName, dbo.DtFinalDiagnosisLog.FinalDiagnosisId, dbo.DtFinalDiagnosis.FinalDiagnosisPriority, dbo.DtFinalDiagnosisLog.State, 
                      dbo.DtFinalDiagnosisLog.Active
FROM         dbo.DtFinalDiagnosisLog INNER JOIN
                      dbo.DtFinalDiagnosis ON dbo.DtFinalDiagnosisLog.FinalDiagnosisId = dbo.DtFinalDiagnosis.FinalDiagnosisId INNER JOIN
                      dbo.DtFamily ON dbo.DtFinalDiagnosis.FamilyId = dbo.DtFamily.FamilyId INNER JOIN
                      dbo.DtApplication ON dbo.DtFamily.ApplicationId = dbo.DtApplication.ApplicationId
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[41] 4[31] 2[10] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "DtFinalDiagnosisLog"
            Begin Extent = 
               Top = 9
               Left = 853
               Bottom = 177
               Right = 1042
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtFinalDiagnosis"
            Begin Extent = 
               Top = 10
               Left = 454
               Bottom = 279
               Right = 790
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtFamily"
            Begin Extent = 
               Top = 11
               Left = 246
               Bottom = 164
               Right = 406
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtApplication"
            Begin Extent = 
               Top = 12
               Left = 5
               Bottom = 203
               Right = 196
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 2280
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 2175
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
     ' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtFinalDiagnosisLogView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane2', @value=N'    Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtFinalDiagnosisLogView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=2 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtFinalDiagnosisLogView'
GO
/****** Object:  Table [dbo].[DtRecommendationDefinition]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DtRecommendationDefinition](
	[RecommendationDefinitionId] [int] IDENTITY(1,1) NOT NULL,
	[FinalDiagnosisId] [int] NOT NULL,
	[QuantOutputId] [int] NOT NULL,
 CONSTRAINT [PK_DtRecommendationDefinition] PRIMARY KEY CLUSTERED 
(
	[RecommendationDefinitionId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[DtRecommendation]    Script Date: 08/15/2019 14:17:33 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
SET ANSI_PADDING ON
GO
CREATE TABLE [dbo].[DtRecommendation](
	[RecommendationId] [int] IDENTITY(1,1) NOT NULL,
	[RecommendationDefinitionId] [int] NOT NULL,
	[DiagnosisEntryId] [int] NOT NULL,
	[Recommendation] [float] NOT NULL,
	[AutoRecommendation] [float] NOT NULL,
	[ManualRecommendation] [float] NULL,
	[AutoOrManual] [varchar](50) NOT NULL,
	[RampTime] [float] NULL,
 CONSTRAINT [PK_DtRecommendation] PRIMARY KEY CLUSTERED 
(
	[RecommendationId] ASC
)WITH (PAD_INDEX  = OFF, STATISTICS_NORECOMPUTE  = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS  = ON, ALLOW_PAGE_LOCKS  = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
SET ANSI_PADDING OFF
GO
/****** Object:  View [dbo].[DtQuantOutputView]    Script Date: 08/15/2019 14:17:34 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [dbo].[DtQuantOutputView]
AS
SELECT     TOP (100) PERCENT dbo.DtFinalDiagnosis.FinalDiagnosisName, dbo.DtFinalDiagnosis.FinalDiagnosisId, dbo.DtQuantOutput.QuantOutputName, dbo.DtQuantOutput.QuantOutputId, 
                      dbo.DtQuantOutput.TagPath
FROM         dbo.DtFinalDiagnosis INNER JOIN
                      dbo.DtRecommendationDefinition ON dbo.DtFinalDiagnosis.FinalDiagnosisId = dbo.DtRecommendationDefinition.FinalDiagnosisId INNER JOIN
                      dbo.DtQuantOutput ON dbo.DtRecommendationDefinition.QuantOutputId = dbo.DtQuantOutput.QuantOutputId
ORDER BY dbo.DtFinalDiagnosis.FinalDiagnosisName, dbo.DtQuantOutput.QuantOutputName
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPane1', @value=N'[0E232FF0-B466-11cf-A24F-00AA00A3EFFF, 1.00]
Begin DesignProperties = 
   Begin PaneConfigurations = 
      Begin PaneConfiguration = 0
         NumPanes = 4
         Configuration = "(H (1[40] 4[20] 2[20] 3) )"
      End
      Begin PaneConfiguration = 1
         NumPanes = 3
         Configuration = "(H (1 [50] 4 [25] 3))"
      End
      Begin PaneConfiguration = 2
         NumPanes = 3
         Configuration = "(H (1 [50] 2 [25] 3))"
      End
      Begin PaneConfiguration = 3
         NumPanes = 3
         Configuration = "(H (4 [30] 2 [40] 3))"
      End
      Begin PaneConfiguration = 4
         NumPanes = 2
         Configuration = "(H (1 [56] 3))"
      End
      Begin PaneConfiguration = 5
         NumPanes = 2
         Configuration = "(H (2 [66] 3))"
      End
      Begin PaneConfiguration = 6
         NumPanes = 2
         Configuration = "(H (4 [50] 3))"
      End
      Begin PaneConfiguration = 7
         NumPanes = 1
         Configuration = "(V (3))"
      End
      Begin PaneConfiguration = 8
         NumPanes = 3
         Configuration = "(H (1[56] 4[18] 2) )"
      End
      Begin PaneConfiguration = 9
         NumPanes = 2
         Configuration = "(H (1 [75] 4))"
      End
      Begin PaneConfiguration = 10
         NumPanes = 2
         Configuration = "(H (1[66] 2) )"
      End
      Begin PaneConfiguration = 11
         NumPanes = 2
         Configuration = "(H (4 [60] 2))"
      End
      Begin PaneConfiguration = 12
         NumPanes = 1
         Configuration = "(H (1) )"
      End
      Begin PaneConfiguration = 13
         NumPanes = 1
         Configuration = "(V (4))"
      End
      Begin PaneConfiguration = 14
         NumPanes = 1
         Configuration = "(V (2))"
      End
      ActivePaneConfig = 0
   End
   Begin DiagramPane = 
      Begin Origin = 
         Top = 0
         Left = 0
      End
      Begin Tables = 
         Begin Table = "DtFinalDiagnosis"
            Begin Extent = 
               Top = 6
               Left = 38
               Bottom = 126
               Right = 374
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtRecommendationDefinition"
            Begin Extent = 
               Top = 11
               Left = 459
               Bottom = 116
               Right = 685
            End
            DisplayFlags = 280
            TopColumn = 0
         End
         Begin Table = "DtQuantOutput"
            Begin Extent = 
               Top = 41
               Left = 771
               Bottom = 211
               Right = 997
            End
            DisplayFlags = 280
            TopColumn = 0
         End
      End
   End
   Begin SQLPane = 
   End
   Begin DataPane = 
      Begin ParameterDefaults = ""
      End
      Begin ColumnWidths = 9
         Width = 284
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
         Width = 1500
      End
   End
   Begin CriteriaPane = 
      Begin ColumnWidths = 11
         Column = 1800
         Alias = 900
         Table = 1170
         Output = 720
         Append = 1400
         NewValue = 1170
         SortType = 1350
         SortOrder = 1410
         GroupBy = 1350
         Filter = 1350
         Or = 1350
         Or = 1350
         Or = 1350
      End
   End
End
' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtQuantOutputView'
GO
EXEC sys.sp_addextendedproperty @name=N'MS_DiagramPaneCount', @value=1 , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'VIEW',@level1name=N'DtQuantOutputView'
GO
/****** Object:  Default [DF_DtApplication_NotificationStrategy]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtApplication] ADD  CONSTRAINT [DF_DtApplication_NotificationStrategy]  DEFAULT ('ocAlert') FOR [NotificationStrategy]
GO
/****** Object:  Default [DF_DtApplication_Managed]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtApplication] ADD  CONSTRAINT [DF_DtApplication_Managed]  DEFAULT ((1)) FOR [Managed]
GO
/****** Object:  Default [DF_DtFinalDiagnosis_Active]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtFinalDiagnosis] ADD  CONSTRAINT [DF_DtFinalDiagnosis_Active]  DEFAULT ((0)) FOR [Active]
GO
/****** Object:  Default [DF_DtFinalDiagnosis_TimeOfMostRecentRecommendationImplementation]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtFinalDiagnosis] ADD  CONSTRAINT [DF_DtFinalDiagnosis_TimeOfMostRecentRecommendationImplementation]  DEFAULT (getdate()) FOR [TimeOfMostRecentRecommendationImplementation]
GO
/****** Object:  Default [DF_DtFinalDiagnosis_ShowExplanationWithRecommendation]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtFinalDiagnosis] ADD  CONSTRAINT [DF_DtFinalDiagnosis_ShowExplanationWithRecommendation]  DEFAULT ((0)) FOR [ShowExplanationWithRecommendation]
GO
/****** Object:  Default [DF_DtQuantOutput_IgnoreMinimumIncrement]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtQuantOutput] ADD  CONSTRAINT [DF_DtQuantOutput_IgnoreMinimumIncrement]  DEFAULT ((0)) FOR [IgnoreMinimumIncrement]
GO
/****** Object:  Default [DF_LtDCSValue_MinimumSampleIntervalSeconds]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDCSValue] ADD  CONSTRAINT [DF_LtDCSValue_MinimumSampleIntervalSeconds]  DEFAULT ((300)) FOR [MinimumSampleIntervalSeconds]
GO
/****** Object:  Default [DF_LtDCSValue_AllowManualEntry]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDCSValue] ADD  CONSTRAINT [DF_LtDCSValue_AllowManualEntry]  DEFAULT ((1)) FOR [AllowManualEntry]
GO
/****** Object:  Default [DF_LtDisplayTableDetails_DisplayOrder]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDisplayTableDetails] ADD  CONSTRAINT [DF_LtDisplayTableDetails_DisplayOrder]  DEFAULT ((0)) FOR [DisplayOrder]
GO
/****** Object:  Default [DF_LtPHDValue_AllowManualEntry]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtPHDValue] ADD  CONSTRAINT [DF_LtPHDValue_AllowManualEntry]  DEFAULT ((1)) FOR [AllowManualEntry]
GO
/****** Object:  Default [DF_QueueMaster_AutoViewSeverityThreshold]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMaster] ADD  CONSTRAINT [DF_QueueMaster_AutoViewSeverityThreshold]  DEFAULT ((10.0)) FOR [AutoViewSeverityThreshold]
GO
/****** Object:  Default [DF_QueueMaster_Position]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMaster] ADD  CONSTRAINT [DF_QueueMaster_Position]  DEFAULT ('center') FOR [Position]
GO
/****** Object:  Default [DF_QueueMaster_AutoViewAdmin]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMaster] ADD  CONSTRAINT [DF_QueueMaster_AutoViewAdmin]  DEFAULT ((0)) FOR [AutoViewAdmin]
GO
/****** Object:  Default [DF_QueueMaster_AutoViewAE]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMaster] ADD  CONSTRAINT [DF_QueueMaster_AutoViewAE]  DEFAULT ((0)) FOR [AutoViewAE]
GO
/****** Object:  Default [DF_QueueMaster_AutoViewOperator]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMaster] ADD  CONSTRAINT [DF_QueueMaster_AutoViewOperator]  DEFAULT ((0)) FOR [AutoViewOperator]
GO
/****** Object:  Default [DF_QueueMessageStatus_Severity]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMessageStatus] ADD  CONSTRAINT [DF_QueueMessageStatus_Severity]  DEFAULT ((0.0)) FOR [Severity]
GO
/****** Object:  Default [DF__RtGradeMa__Times__7A672E12]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtGradeMaster] ADD  CONSTRAINT [DF__RtGradeMa__Times__7A672E12]  DEFAULT (getdate()) FOR [Timestamp]
GO
/****** Object:  Default [DF__RtGradeMa__Activ__7B5B524B]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtGradeMaster] ADD  CONSTRAINT [DF__RtGradeMa__Activ__7B5B524B]  DEFAULT ((0)) FOR [Active]
GO
/****** Object:  Default [DF_SfcChart_IsProduction]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcChart] ADD  CONSTRAINT [DF_SfcChart_IsProduction]  DEFAULT ((1)) FOR [IsProduction]
GO
/****** Object:  Default [DF_SfcRecipeDataFolder_RecipeDataType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataFolder] ADD  CONSTRAINT [DF_SfcRecipeDataFolder_RecipeDataType]  DEFAULT ('Folder') FOR [RecipeDataType]
GO
/****** Object:  Default [DF_TkConsole_Priority]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkConsole] ADD  CONSTRAINT [DF_TkConsole_Priority]  DEFAULT ((1)) FOR [Priority]
GO
/****** Object:  Default [DF__Units__m__178D7CA5]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[Units] ADD  CONSTRAINT [DF__Units__m__178D7CA5]  DEFAULT ((0)) FOR [m]
GO
/****** Object:  Default [DF__Units__b__1881A0DE]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[Units] ADD  CONSTRAINT [DF__Units__b__1881A0DE]  DEFAULT ((0)) FOR [b]
GO
/****** Object:  ForeignKey [FK_BtBatchLog_BtBatchRun]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[BtBatchLog]  WITH CHECK ADD  CONSTRAINT [FK_BtBatchLog_BtBatchRun] FOREIGN KEY([BatchRunId])
REFERENCES [dbo].[BtBatchRun] ([BatchRunId])
GO
ALTER TABLE [dbo].[BtBatchLog] CHECK CONSTRAINT [FK_BtBatchLog_BtBatchRun]
GO
/****** Object:  ForeignKey [FK_BtBatchRun_BtReactor]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[BtBatchRun]  WITH CHECK ADD  CONSTRAINT [FK_BtBatchRun_BtReactor] FOREIGN KEY([ReactorId])
REFERENCES [dbo].[BtReactor] ([ReactorId])
GO
ALTER TABLE [dbo].[BtBatchRun] CHECK CONSTRAINT [FK_BtBatchRun_BtReactor]
GO
/****** Object:  ForeignKey [FK_BtStripperBatchLog_BtBatchRun]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[BtStripperBatchLog]  WITH CHECK ADD  CONSTRAINT [FK_BtStripperBatchLog_BtBatchRun] FOREIGN KEY([BatchRunId])
REFERENCES [dbo].[BtBatchRun] ([BatchRunId])
GO
ALTER TABLE [dbo].[BtStripperBatchLog] CHECK CONSTRAINT [FK_BtStripperBatchLog_BtBatchRun]
GO
/****** Object:  ForeignKey [FK_DtApplication_Lookup]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtApplication]  WITH CHECK ADD  CONSTRAINT [FK_DtApplication_Lookup] FOREIGN KEY([GroupRampMethodId])
REFERENCES [dbo].[Lookup] ([LookupId])
GO
ALTER TABLE [dbo].[DtApplication] CHECK CONSTRAINT [FK_DtApplication_Lookup]
GO
/****** Object:  ForeignKey [FK_DtApplication_QueueMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtApplication]  WITH CHECK ADD  CONSTRAINT [FK_DtApplication_QueueMaster] FOREIGN KEY([MessageQueueId])
REFERENCES [dbo].[QueueMaster] ([QueueId])
GO
ALTER TABLE [dbo].[DtApplication] CHECK CONSTRAINT [FK_DtApplication_QueueMaster]
GO
/****** Object:  ForeignKey [FK_DtApplication_TkUnit]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtApplication]  WITH CHECK ADD  CONSTRAINT [FK_DtApplication_TkUnit] FOREIGN KEY([UnitId])
REFERENCES [dbo].[TkUnit] ([UnitId])
GO
ALTER TABLE [dbo].[DtApplication] CHECK CONSTRAINT [FK_DtApplication_TkUnit]
GO
/****** Object:  ForeignKey [FK_DtFamily_DtApplication]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtFamily]  WITH CHECK ADD  CONSTRAINT [FK_DtFamily_DtApplication] FOREIGN KEY([ApplicationId])
REFERENCES [dbo].[DtApplication] ([ApplicationId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtFamily] CHECK CONSTRAINT [FK_DtFamily_DtApplication]
GO
/****** Object:  ForeignKey [FK_DtFinalDiagnosis_DtFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtFinalDiagnosis]  WITH CHECK ADD  CONSTRAINT [FK_DtFinalDiagnosis_DtFamily] FOREIGN KEY([FamilyId])
REFERENCES [dbo].[DtFamily] ([FamilyId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtFinalDiagnosis] CHECK CONSTRAINT [FK_DtFinalDiagnosis_DtFamily]
GO
/****** Object:  ForeignKey [FK_DtQuantOutput_DtApplication]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtQuantOutput]  WITH CHECK ADD  CONSTRAINT [FK_DtQuantOutput_DtApplication] FOREIGN KEY([ApplicationId])
REFERENCES [dbo].[DtApplication] ([ApplicationId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtQuantOutput] CHECK CONSTRAINT [FK_DtQuantOutput_DtApplication]
GO
/****** Object:  ForeignKey [FK_DtQuantOutput_Lookup]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtQuantOutput]  WITH CHECK ADD  CONSTRAINT [FK_DtQuantOutput_Lookup] FOREIGN KEY([FeedbackMethodId])
REFERENCES [dbo].[Lookup] ([LookupId])
GO
ALTER TABLE [dbo].[DtQuantOutput] CHECK CONSTRAINT [FK_DtQuantOutput_Lookup]
GO
/****** Object:  ForeignKey [FK_DtQuantOutputRamp_DtQuantOutput]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtQuantOutputRamp]  WITH CHECK ADD  CONSTRAINT [FK_DtQuantOutputRamp_DtQuantOutput] FOREIGN KEY([QuantOutputId])
REFERENCES [dbo].[DtQuantOutput] ([QuantOutputId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtQuantOutputRamp] CHECK CONSTRAINT [FK_DtQuantOutputRamp_DtQuantOutput]
GO
/****** Object:  ForeignKey [FK_DtRecommendation_DtDiagnosisEntry]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtRecommendation]  WITH CHECK ADD  CONSTRAINT [FK_DtRecommendation_DtDiagnosisEntry] FOREIGN KEY([DiagnosisEntryId])
REFERENCES [dbo].[DtDiagnosisEntry] ([DiagnosisEntryId])
GO
ALTER TABLE [dbo].[DtRecommendation] CHECK CONSTRAINT [FK_DtRecommendation_DtDiagnosisEntry]
GO
/****** Object:  ForeignKey [FK_DtRecommendation_DtRecommendationDefinition]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtRecommendation]  WITH CHECK ADD  CONSTRAINT [FK_DtRecommendation_DtRecommendationDefinition] FOREIGN KEY([RecommendationDefinitionId])
REFERENCES [dbo].[DtRecommendationDefinition] ([RecommendationDefinitionId])
GO
ALTER TABLE [dbo].[DtRecommendation] CHECK CONSTRAINT [FK_DtRecommendation_DtRecommendationDefinition]
GO
/****** Object:  ForeignKey [FK_DtRecommendationDefinition_DtFinalDiagnosis]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtRecommendationDefinition]  WITH CHECK ADD  CONSTRAINT [FK_DtRecommendationDefinition_DtFinalDiagnosis] FOREIGN KEY([FinalDiagnosisId])
REFERENCES [dbo].[DtFinalDiagnosis] ([FinalDiagnosisId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtRecommendationDefinition] CHECK CONSTRAINT [FK_DtRecommendationDefinition_DtFinalDiagnosis]
GO
/****** Object:  ForeignKey [FK_DtRecommendationDefinition_DtQuantOutput]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtRecommendationDefinition]  WITH CHECK ADD  CONSTRAINT [FK_DtRecommendationDefinition_DtQuantOutput] FOREIGN KEY([QuantOutputId])
REFERENCES [dbo].[DtQuantOutput] ([QuantOutputId])
GO
ALTER TABLE [dbo].[DtRecommendationDefinition] CHECK CONSTRAINT [FK_DtRecommendationDefinition_DtQuantOutput]
GO
/****** Object:  ForeignKey [FK_DtSQCDiagnosis_DtFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtSQCDiagnosis]  WITH CHECK ADD  CONSTRAINT [FK_DtSQCDiagnosis_DtFamily] FOREIGN KEY([FamilyId])
REFERENCES [dbo].[DtFamily] ([FamilyId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtSQCDiagnosis] CHECK CONSTRAINT [FK_DtSQCDiagnosis_DtFamily]
GO
/****** Object:  ForeignKey [FK_DtTextRecommendation_DtDiagnosisEntry]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[DtTextRecommendation]  WITH CHECK ADD  CONSTRAINT [FK_DtTextRecommendation_DtDiagnosisEntry] FOREIGN KEY([DiagnosisEntryId])
REFERENCES [dbo].[DtDiagnosisEntry] ([DiagnosisEntryId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[DtTextRecommendation] CHECK CONSTRAINT [FK_DtTextRecommendation_DtDiagnosisEntry]
GO
/****** Object:  ForeignKey [FK_Lookup_LookupType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[Lookup]  WITH CHECK ADD  CONSTRAINT [FK_Lookup_LookupType] FOREIGN KEY([LookupTypeCode])
REFERENCES [dbo].[LookupType] ([LookupTypeCode])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[Lookup] CHECK CONSTRAINT [FK_Lookup_LookupType]
GO
/****** Object:  ForeignKey [FK_LtDCSValue_LtOPCInterface]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDCSValue]  WITH CHECK ADD  CONSTRAINT [FK_LtDCSValue_LtOPCInterface] FOREIGN KEY([InterfaceId])
REFERENCES [dbo].[LtOPCInterface] ([InterfaceId])
GO
ALTER TABLE [dbo].[LtDCSValue] CHECK CONSTRAINT [FK_LtDCSValue_LtOPCInterface]
GO
/****** Object:  ForeignKey [FK_LtDCSValue_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDCSValue]  WITH CHECK ADD  CONSTRAINT [FK_LtDCSValue_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtDCSValue] CHECK CONSTRAINT [FK_LtDCSValue_LtValue]
GO
/****** Object:  ForeignKey [FK_LtDerivedValue_LtHDAInterface]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDerivedValue]  WITH CHECK ADD  CONSTRAINT [FK_LtDerivedValue_LtHDAInterface] FOREIGN KEY([ResultInterfaceId])
REFERENCES [dbo].[LtHDAInterface] ([InterfaceId])
GO
ALTER TABLE [dbo].[LtDerivedValue] CHECK CONSTRAINT [FK_LtDerivedValue_LtHDAInterface]
GO
/****** Object:  ForeignKey [FK_LtDerivedValue_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDerivedValue]  WITH CHECK ADD  CONSTRAINT [FK_LtDerivedValue_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtDerivedValue] CHECK CONSTRAINT [FK_LtDerivedValue_LtValue]
GO
/****** Object:  ForeignKey [FK_LtDerivedValue_LtValue1]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDerivedValue]  WITH CHECK ADD  CONSTRAINT [FK_LtDerivedValue_LtValue1] FOREIGN KEY([TriggerValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtDerivedValue] CHECK CONSTRAINT [FK_LtDerivedValue_LtValue1]
GO
/****** Object:  ForeignKey [FK_LtDisplayTable_TkPost]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDisplayTable]  WITH CHECK ADD  CONSTRAINT [FK_LtDisplayTable_TkPost] FOREIGN KEY([PostId])
REFERENCES [dbo].[TkPost] ([PostId])
GO
ALTER TABLE [dbo].[LtDisplayTable] CHECK CONSTRAINT [FK_LtDisplayTable_TkPost]
GO
/****** Object:  ForeignKey [FK_LtDisplayTableDetails_LtDisplayTable]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDisplayTableDetails]  WITH CHECK ADD  CONSTRAINT [FK_LtDisplayTableDetails_LtDisplayTable] FOREIGN KEY([DisplayTableId])
REFERENCES [dbo].[LtDisplayTable] ([DisplayTableId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[LtDisplayTableDetails] CHECK CONSTRAINT [FK_LtDisplayTableDetails_LtDisplayTable]
GO
/****** Object:  ForeignKey [FK_LtDisplayTableDetails_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtDisplayTableDetails]  WITH CHECK ADD  CONSTRAINT [FK_LtDisplayTableDetails_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[LtDisplayTableDetails] CHECK CONSTRAINT [FK_LtDisplayTableDetails_LtValue]
GO
/****** Object:  ForeignKey [FK_LtHistory_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtHistory]  WITH CHECK ADD  CONSTRAINT [FK_LtHistory_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtHistory] CHECK CONSTRAINT [FK_LtHistory_LtValue]
GO
/****** Object:  ForeignKey [FK_LtLimit_Lookup]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtLimit]  WITH CHECK ADD  CONSTRAINT [FK_LtLimit_Lookup] FOREIGN KEY([LimitTypeId])
REFERENCES [dbo].[Lookup] ([LookupId])
GO
ALTER TABLE [dbo].[LtLimit] CHECK CONSTRAINT [FK_LtLimit_Lookup]
GO
/****** Object:  ForeignKey [FK_LtLimit_Lookup1]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtLimit]  WITH CHECK ADD  CONSTRAINT [FK_LtLimit_Lookup1] FOREIGN KEY([LimitSourceId])
REFERENCES [dbo].[Lookup] ([LookupId])
GO
ALTER TABLE [dbo].[LtLimit] CHECK CONSTRAINT [FK_LtLimit_Lookup1]
GO
/****** Object:  ForeignKey [FK_LtLimit_LtOPCInterface]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtLimit]  WITH CHECK ADD  CONSTRAINT [FK_LtLimit_LtOPCInterface] FOREIGN KEY([OPCInterfaceId])
REFERENCES [dbo].[LtOPCInterface] ([InterfaceId])
GO
ALTER TABLE [dbo].[LtLimit] CHECK CONSTRAINT [FK_LtLimit_LtOPCInterface]
GO
/****** Object:  ForeignKey [FK_LtLimit_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtLimit]  WITH CHECK ADD  CONSTRAINT [FK_LtLimit_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtLimit] CHECK CONSTRAINT [FK_LtLimit_LtValue]
GO
/****** Object:  ForeignKey [FK_LtLocalValue_LtHDAInterface]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtLocalValue]  WITH CHECK ADD  CONSTRAINT [FK_LtLocalValue_LtHDAInterface] FOREIGN KEY([InterfaceId])
REFERENCES [dbo].[LtHDAInterface] ([InterfaceId])
GO
ALTER TABLE [dbo].[LtLocalValue] CHECK CONSTRAINT [FK_LtLocalValue_LtHDAInterface]
GO
/****** Object:  ForeignKey [FK_LtLocalValue_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtLocalValue]  WITH CHECK ADD  CONSTRAINT [FK_LtLocalValue_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtLocalValue] CHECK CONSTRAINT [FK_LtLocalValue_LtValue]
GO
/****** Object:  ForeignKey [FK_LtPHDLabValue_LtLabValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtPHDValue]  WITH CHECK ADD  CONSTRAINT [FK_LtPHDLabValue_LtLabValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtPHDValue] CHECK CONSTRAINT [FK_LtPHDLabValue_LtLabValue]
GO
/****** Object:  ForeignKey [FK_LtPHDValue_LtHDAInterface]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtPHDValue]  WITH CHECK ADD  CONSTRAINT [FK_LtPHDValue_LtHDAInterface] FOREIGN KEY([InterfaceId])
REFERENCES [dbo].[LtHDAInterface] ([InterfaceId])
GO
ALTER TABLE [dbo].[LtPHDValue] CHECK CONSTRAINT [FK_LtPHDValue_LtHDAInterface]
GO
/****** Object:  ForeignKey [FK_LtRelatedData_LtDerivedValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtRelatedData]  WITH CHECK ADD  CONSTRAINT [FK_LtRelatedData_LtDerivedValue] FOREIGN KEY([DerivedValueId])
REFERENCES [dbo].[LtDerivedValue] ([DerivedValueId])
GO
ALTER TABLE [dbo].[LtRelatedData] CHECK CONSTRAINT [FK_LtRelatedData_LtDerivedValue]
GO
/****** Object:  ForeignKey [FK_LtRelatedData_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtRelatedData]  WITH CHECK ADD  CONSTRAINT [FK_LtRelatedData_LtValue] FOREIGN KEY([RelatedValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtRelatedData] CHECK CONSTRAINT [FK_LtRelatedData_LtValue]
GO
/****** Object:  ForeignKey [FK_LtSelector_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtSelector]  WITH CHECK ADD  CONSTRAINT [FK_LtSelector_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtSelector] CHECK CONSTRAINT [FK_LtSelector_LtValue]
GO
/****** Object:  ForeignKey [FK_LtValue_TkUnit]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtValue]  WITH CHECK ADD  CONSTRAINT [FK_LtValue_TkUnit] FOREIGN KEY([UnitId])
REFERENCES [dbo].[TkUnit] ([UnitId])
GO
ALTER TABLE [dbo].[LtValue] CHECK CONSTRAINT [FK_LtValue_TkUnit]
GO
/****** Object:  ForeignKey [FK_LtValueViewed_LtValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[LtValueViewed]  WITH CHECK ADD  CONSTRAINT [FK_LtValueViewed_LtValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[LtValue] ([ValueId])
GO
ALTER TABLE [dbo].[LtValueViewed] CHECK CONSTRAINT [FK_LtValueViewed_LtValue]
GO
/****** Object:  ForeignKey [FK_QueueDetail_QueueMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueDetail]  WITH CHECK ADD  CONSTRAINT [FK_QueueDetail_QueueMaster] FOREIGN KEY([QueueId])
REFERENCES [dbo].[QueueMaster] ([QueueId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[QueueDetail] CHECK CONSTRAINT [FK_QueueDetail_QueueMaster]
GO
/****** Object:  ForeignKey [FK_QueueDetail_QueueMessageStatus]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueDetail]  WITH CHECK ADD  CONSTRAINT [FK_QueueDetail_QueueMessageStatus] FOREIGN KEY([StatusId])
REFERENCES [dbo].[QueueMessageStatus] ([StatusId])
GO
ALTER TABLE [dbo].[QueueDetail] CHECK CONSTRAINT [FK_QueueDetail_QueueMessageStatus]
GO
/****** Object:  ForeignKey [UK_QueueMaster_QueueMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[QueueMaster]  WITH CHECK ADD  CONSTRAINT [UK_QueueMaster_QueueMaster] FOREIGN KEY([QueueId])
REFERENCES [dbo].[QueueMaster] ([QueueId])
GO
ALTER TABLE [dbo].[QueueMaster] CHECK CONSTRAINT [UK_QueueMaster_QueueMaster]
GO
/****** Object:  ForeignKey [FK_RtDownloadMaster_RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtDownloadMaster]  WITH CHECK ADD  CONSTRAINT [FK_RtDownloadMaster_RtRecipeFamily] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtDownloadMaster] CHECK CONSTRAINT [FK_RtDownloadMaster_RtRecipeFamily]
GO
/****** Object:  ForeignKey [FK_RtEvent_RtEventParameter]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtEvent]  WITH CHECK ADD  CONSTRAINT [FK_RtEvent_RtEventParameter] FOREIGN KEY([ParameterId])
REFERENCES [dbo].[RtEventParameter] ([ParameterId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtEvent] CHECK CONSTRAINT [FK_RtEvent_RtEventParameter]
GO
/****** Object:  ForeignKey [FK_RtEventParameter_RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtEventParameter]  WITH CHECK ADD  CONSTRAINT [FK_RtEventParameter_RtRecipeFamily] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtEventParameter] CHECK CONSTRAINT [FK_RtEventParameter_RtRecipeFamily]
GO
/****** Object:  ForeignKey [FK_RtGain_RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtGain]  WITH CHECK ADD  CONSTRAINT [FK_RtGain_RtRecipeFamily] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtGain] CHECK CONSTRAINT [FK_RtGain_RtRecipeFamily]
GO
/****** Object:  ForeignKey [FK_RtGainGrade_RtGain]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtGainGrade]  WITH CHECK ADD  CONSTRAINT [FK_RtGainGrade_RtGain] FOREIGN KEY([ParameterId])
REFERENCES [dbo].[RtGain] ([ParameterId])
GO
ALTER TABLE [dbo].[RtGainGrade] CHECK CONSTRAINT [FK_RtGainGrade_RtGain]
GO
/****** Object:  ForeignKey [FK_RtGradeDetail_RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtGradeDetail]  WITH CHECK ADD  CONSTRAINT [FK_RtGradeDetail_RtRecipeFamily] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtGradeDetail] CHECK CONSTRAINT [FK_RtGradeDetail_RtRecipeFamily]
GO
/****** Object:  ForeignKey [FK_RtGradeMaster_RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtGradeMaster]  WITH CHECK ADD  CONSTRAINT [FK_RtGradeMaster_RtRecipeFamily] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtGradeMaster] CHECK CONSTRAINT [FK_RtGradeMaster_RtRecipeFamily]
GO
/****** Object:  ForeignKey [FK_RtRecipeFamily_TkPost]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtRecipeFamily]  WITH CHECK ADD  CONSTRAINT [FK_RtRecipeFamily_TkPost] FOREIGN KEY([PostId])
REFERENCES [dbo].[TkPost] ([PostId])
GO
ALTER TABLE [dbo].[RtRecipeFamily] CHECK CONSTRAINT [FK_RtRecipeFamily_TkPost]
GO
/****** Object:  ForeignKey [FK_RtSQCLimit_RtSQCParameter]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtSQCLimit]  WITH CHECK ADD  CONSTRAINT [FK_RtSQCLimit_RtSQCParameter] FOREIGN KEY([ParameterId])
REFERENCES [dbo].[RtSQCParameter] ([ParameterId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtSQCLimit] CHECK CONSTRAINT [FK_RtSQCLimit_RtSQCParameter]
GO
/****** Object:  ForeignKey [FK_RtSQCParameter_RtRecipeFamily]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtSQCParameter]  WITH CHECK ADD  CONSTRAINT [FK_RtSQCParameter_RtRecipeFamily] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtSQCParameter] CHECK CONSTRAINT [FK_RtSQCParameter_RtRecipeFamily]
GO
/****** Object:  ForeignKey [FK_RtValueDefinition_RtValueDefinition]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtValueDefinition]  WITH CHECK ADD  CONSTRAINT [FK_RtValueDefinition_RtValueDefinition] FOREIGN KEY([RecipeFamilyId])
REFERENCES [dbo].[RtRecipeFamily] ([RecipeFamilyId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[RtValueDefinition] CHECK CONSTRAINT [FK_RtValueDefinition_RtValueDefinition]
GO
/****** Object:  ForeignKey [FK_RtValueDefinition_RtValueType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtValueDefinition]  WITH CHECK ADD  CONSTRAINT [FK_RtValueDefinition_RtValueType] FOREIGN KEY([ValueTypeId])
REFERENCES [dbo].[RtValueType] ([ValueTypeId])
GO
ALTER TABLE [dbo].[RtValueDefinition] CHECK CONSTRAINT [FK_RtValueDefinition_RtValueType]
GO
/****** Object:  ForeignKey [FK_RtValueDefinition_RtWriteLocation]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[RtValueDefinition]  WITH CHECK ADD  CONSTRAINT [FK_RtValueDefinition_RtWriteLocation] FOREIGN KEY([WriteLocationId])
REFERENCES [dbo].[TkWriteLocation] ([WriteLocationId])
GO
ALTER TABLE [dbo].[RtValueDefinition] CHECK CONSTRAINT [FK_RtValueDefinition_RtWriteLocation]
GO
/****** Object:  ForeignKey [FK_SfcBusyNotification_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcBusyNotification]  WITH CHECK ADD  CONSTRAINT [FK_SfcBusyNotification_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcBusyNotification] CHECK CONSTRAINT [FK_SfcBusyNotification_SfcWindow]
GO
/****** Object:  ForeignKey [FK_SfcControlPanel_TkPost]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcControlPanel]  WITH CHECK ADD  CONSTRAINT [FK_SfcControlPanel_TkPost] FOREIGN KEY([PostId])
REFERENCES [dbo].[TkPost] ([PostId])
GO
ALTER TABLE [dbo].[SfcControlPanel] CHECK CONSTRAINT [FK_SfcControlPanel_TkPost]
GO
/****** Object:  ForeignKey [FK_SfcControlPanelMessage_SfcControlPanel]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcControlPanelMessage]  WITH CHECK ADD  CONSTRAINT [FK_SfcControlPanelMessage_SfcControlPanel] FOREIGN KEY([controlPanelId])
REFERENCES [dbo].[SfcControlPanel] ([ControlPanelId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcControlPanelMessage] CHECK CONSTRAINT [FK_SfcControlPanelMessage_SfcControlPanel]
GO
/****** Object:  ForeignKey [FK_SfcDownloadGUI_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcDownloadGUI]  WITH CHECK ADD  CONSTRAINT [FK_SfcDownloadGUI_SfcWindow] FOREIGN KEY([WindowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcDownloadGUI] CHECK CONSTRAINT [FK_SfcDownloadGUI_SfcWindow]
GO
/****** Object:  ForeignKey [FK_SfcDownloadGUITable_SfcDownloadGUI]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcDownloadGUITable]  WITH CHECK ADD  CONSTRAINT [FK_SfcDownloadGUITable_SfcDownloadGUI] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcDownloadGUI] ([WindowId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcDownloadGUITable] CHECK CONSTRAINT [FK_SfcDownloadGUITable_SfcDownloadGUI]
GO
/****** Object:  ForeignKey [FK_SfcHierarchy_SfcChart]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcHierarchy]  WITH CHECK ADD  CONSTRAINT [FK_SfcHierarchy_SfcChart] FOREIGN KEY([ChartId])
REFERENCES [dbo].[SfcChart] ([ChartId])
GO
ALTER TABLE [dbo].[SfcHierarchy] CHECK CONSTRAINT [FK_SfcHierarchy_SfcChart]
GO
/****** Object:  ForeignKey [FK_SfcHierarchy_SfcChart1]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcHierarchy]  WITH CHECK ADD  CONSTRAINT [FK_SfcHierarchy_SfcChart1] FOREIGN KEY([ChildChartId])
REFERENCES [dbo].[SfcChart] ([ChartId])
GO
ALTER TABLE [dbo].[SfcHierarchy] CHECK CONSTRAINT [FK_SfcHierarchy_SfcChart1]
GO
/****** Object:  ForeignKey [FK_SfcHierarchy_SfcStep]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcHierarchy]  WITH CHECK ADD  CONSTRAINT [FK_SfcHierarchy_SfcStep] FOREIGN KEY([StepId])
REFERENCES [dbo].[SfcStep] ([StepId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcHierarchy] CHECK CONSTRAINT [FK_SfcHierarchy_SfcStep]
GO
/****** Object:  ForeignKey [FK_SfcChartHandler_SfcChart]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcHierarchyHandler]  WITH CHECK ADD  CONSTRAINT [FK_SfcChartHandler_SfcChart] FOREIGN KEY([ChartId])
REFERENCES [dbo].[SfcChart] ([ChartId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcHierarchyHandler] CHECK CONSTRAINT [FK_SfcChartHandler_SfcChart]
GO
/****** Object:  ForeignKey [FK_SfcInput_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcInput]  WITH CHECK ADD  CONSTRAINT [FK_SfcInput_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcInput] CHECK CONSTRAINT [FK_SfcInput_SfcWindow]
GO
/****** Object:  ForeignKey [FK_SfcManualDataEntry_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcManualDataEntry]  WITH CHECK ADD  CONSTRAINT [FK_SfcManualDataEntry_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
GO
ALTER TABLE [dbo].[SfcManualDataEntry] CHECK CONSTRAINT [FK_SfcManualDataEntry_SfcWindow]
GO
/****** Object:  ForeignKey [FK__SfcManual__windo__5AB9788F]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcManualDataEntryTable]  WITH CHECK ADD  CONSTRAINT [FK__SfcManual__windo__5AB9788F] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcManualDataEntry] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcManualDataEntryTable] CHECK CONSTRAINT [FK__SfcManual__windo__5AB9788F]
GO
/****** Object:  ForeignKey [FK_SfcRecipeData_SfcRecipeDataType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeData]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeData_SfcRecipeDataType] FOREIGN KEY([RecipeDataTypeId])
REFERENCES [dbo].[SfcRecipeDataType] ([RecipeDataTypeId])
GO
ALTER TABLE [dbo].[SfcRecipeData] CHECK CONSTRAINT [FK_SfcRecipeData_SfcRecipeDataType]
GO
/****** Object:  ForeignKey [FK_SfcRecipeData_SfcStep]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeData]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeData_SfcStep] FOREIGN KEY([StepId])
REFERENCES [dbo].[SfcStep] ([StepId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeData] CHECK CONSTRAINT [FK_SfcRecipeData_SfcStep]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataArray_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataArray]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataArray_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataArray] CHECK CONSTRAINT [FK_SfcRecipeDataArray_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataArray_SfcRecipeDataKeyMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataArray]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataArray_SfcRecipeDataKeyMaster] FOREIGN KEY([IndexKeyId])
REFERENCES [dbo].[SfcRecipeDataKeyMaster] ([KeyId])
GO
ALTER TABLE [dbo].[SfcRecipeDataArray] CHECK CONSTRAINT [FK_SfcRecipeDataArray_SfcRecipeDataKeyMaster]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataArray_SfcValueType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataArray]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataArray_SfcValueType] FOREIGN KEY([ValueTypeId])
REFERENCES [dbo].[SfcValueType] ([ValueTypeId])
GO
ALTER TABLE [dbo].[SfcRecipeDataArray] CHECK CONSTRAINT [FK_SfcRecipeDataArray_SfcValueType]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataArrayElement_SfcRecipeDataArray]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataArrayElement]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataArrayElement_SfcRecipeDataArray] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeDataArray] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataArrayElement] CHECK CONSTRAINT [FK_SfcRecipeDataArrayElement_SfcRecipeDataArray]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataArrayElement_SfcRecipeDataValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataArrayElement]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataArrayElement_SfcRecipeDataValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataArrayElement] CHECK CONSTRAINT [FK_SfcRecipeDataArrayElement_SfcRecipeDataValue]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataFolder_SfcStep]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataFolder]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataFolder_SfcStep] FOREIGN KEY([StepId])
REFERENCES [dbo].[SfcStep] ([StepId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataFolder] CHECK CONSTRAINT [FK_SfcRecipeDataFolder_SfcStep]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataInput_PVValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataInput]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataInput_PVValue] FOREIGN KEY([PVValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataInput] CHECK CONSTRAINT [FK_SfcRecipeDataInput_PVValue]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataInput_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataInput]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataInput_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataInput] CHECK CONSTRAINT [FK_SfcRecipeDataInput_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataInput_SfcValueType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataInput]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataInput_SfcValueType] FOREIGN KEY([ValueTypeId])
REFERENCES [dbo].[SfcValueType] ([ValueTypeId])
GO
ALTER TABLE [dbo].[SfcRecipeDataInput] CHECK CONSTRAINT [FK_SfcRecipeDataInput_SfcValueType]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataInput_TargetValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataInput]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataInput_TargetValue] FOREIGN KEY([TargetValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataInput] CHECK CONSTRAINT [FK_SfcRecipeDataInput_TargetValue]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataKeyDetail_SfcRecipeDataKeyMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataKeyDetail]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataKeyDetail_SfcRecipeDataKeyMaster] FOREIGN KEY([KeyId])
REFERENCES [dbo].[SfcRecipeDataKeyMaster] ([KeyId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataKeyDetail] CHECK CONSTRAINT [FK_SfcRecipeDataKeyDetail_SfcRecipeDataKeyMaster]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataMatrix_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataMatrix]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataMatrix] CHECK CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataMatrix_SfcRecipeDataKeyMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataMatrix]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeDataKeyMaster] FOREIGN KEY([RowIndexKeyId])
REFERENCES [dbo].[SfcRecipeDataKeyMaster] ([KeyId])
GO
ALTER TABLE [dbo].[SfcRecipeDataMatrix] CHECK CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeDataKeyMaster]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataMatrix_SfcRecipeDataKeyMaster1]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataMatrix]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeDataKeyMaster1] FOREIGN KEY([ColumnIndexKeyId])
REFERENCES [dbo].[SfcRecipeDataKeyMaster] ([KeyId])
GO
ALTER TABLE [dbo].[SfcRecipeDataMatrix] CHECK CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeDataKeyMaster1]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataMatrix_SfcRecipeDataMatrix]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataMatrix]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeDataMatrix] FOREIGN KEY([ValueTypeId])
REFERENCES [dbo].[SfcValueType] ([ValueTypeId])
GO
ALTER TABLE [dbo].[SfcRecipeDataMatrix] CHECK CONSTRAINT [FK_SfcRecipeDataMatrix_SfcRecipeDataMatrix]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataMatrixElement_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataMatrixElement]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataMatrixElement_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataMatrixElement] CHECK CONSTRAINT [FK_SfcRecipeDataMatrixElement_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataMatrixElement_SfcRecipeDataValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataMatrixElement]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataMatrixElement_SfcRecipeDataValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataMatrixElement] CHECK CONSTRAINT [FK_SfcRecipeDataMatrixElement_SfcRecipeDataValue]
GO
/****** Object:  ForeignKey [FK_Output_Value]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataOutput]  WITH CHECK ADD  CONSTRAINT [FK_Output_Value] FOREIGN KEY([OutputValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataOutput] CHECK CONSTRAINT [FK_Output_Value]
GO
/****** Object:  ForeignKey [FK_PV_Value]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataOutput]  WITH CHECK ADD  CONSTRAINT [FK_PV_Value] FOREIGN KEY([PVValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataOutput] CHECK CONSTRAINT [FK_PV_Value]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataOutput_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataOutput]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataOutput_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataOutput] CHECK CONSTRAINT [FK_SfcRecipeDataOutput_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataOutput_SfcValueType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataOutput]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataOutput_SfcValueType] FOREIGN KEY([ValueTypeId])
REFERENCES [dbo].[SfcValueType] ([ValueTypeId])
GO
ALTER TABLE [dbo].[SfcRecipeDataOutput] CHECK CONSTRAINT [FK_SfcRecipeDataOutput_SfcValueType]
GO
/****** Object:  ForeignKey [FK_Target_Value]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataOutput]  WITH CHECK ADD  CONSTRAINT [FK_Target_Value] FOREIGN KEY([TargetValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataOutput] CHECK CONSTRAINT [FK_Target_Value]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataOutputRamp_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataOutputRamp]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataOutputRamp_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataOutputRamp] CHECK CONSTRAINT [FK_SfcRecipeDataOutputRamp_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataRecipe_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataRecipe]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataRecipe_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataRecipe] CHECK CONSTRAINT [FK_SfcRecipeDataRecipe_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataSimpleValue_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataSimpleValue]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataSimpleValue_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataSimpleValue] CHECK CONSTRAINT [FK_SfcRecipeDataSimpleValue_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataSimpleValue_SfcRecipeDataValue]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataSimpleValue]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataSimpleValue_SfcRecipeDataValue] FOREIGN KEY([ValueId])
REFERENCES [dbo].[SfcRecipeDataValue] ([ValueId])
GO
ALTER TABLE [dbo].[SfcRecipeDataSimpleValue] CHECK CONSTRAINT [FK_SfcRecipeDataSimpleValue_SfcRecipeDataValue]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataSQC_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataSQC]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataSQC_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataSQC] CHECK CONSTRAINT [FK_SfcRecipeDataSQC_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcRecipeDataTimer_SfcRecipeData]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcRecipeDataTimer]  WITH CHECK ADD  CONSTRAINT [FK_SfcRecipeDataTimer_SfcRecipeData] FOREIGN KEY([RecipeDataId])
REFERENCES [dbo].[SfcRecipeData] ([RecipeDataId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcRecipeDataTimer] CHECK CONSTRAINT [FK_SfcRecipeDataTimer_SfcRecipeData]
GO
/****** Object:  ForeignKey [FK_SfcReviewData_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcReviewData]  WITH CHECK ADD  CONSTRAINT [FK_SfcReviewData_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcReviewData] CHECK CONSTRAINT [FK_SfcReviewData_SfcWindow]
GO
/****** Object:  ForeignKey [FK__SfcReview__windo__5F7E2DAC]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcReviewDataTable]  WITH CHECK ADD  CONSTRAINT [FK__SfcReview__windo__5F7E2DAC] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcReviewData] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcReviewDataTable] CHECK CONSTRAINT [FK__SfcReview__windo__5F7E2DAC]
GO
/****** Object:  ForeignKey [FK_SfcReviewFlows_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcReviewFlows]  WITH CHECK ADD  CONSTRAINT [FK_SfcReviewFlows_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcReviewFlows] CHECK CONSTRAINT [FK_SfcReviewFlows_SfcWindow]
GO
/****** Object:  ForeignKey [FK__SfcReview__windo__6442E2C9]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcReviewFlowsTable]  WITH CHECK ADD  CONSTRAINT [FK__SfcReview__windo__6442E2C9] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcReviewFlows] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcReviewFlowsTable] CHECK CONSTRAINT [FK__SfcReview__windo__6442E2C9]
GO
/****** Object:  ForeignKey [FK_SfcSelectInput_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcSelectInput]  WITH CHECK ADD  CONSTRAINT [FK_SfcSelectInput_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcSelectInput] CHECK CONSTRAINT [FK_SfcSelectInput_SfcWindow]
GO
/****** Object:  ForeignKey [FK_SfcStep_SfcChart]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcStep]  WITH CHECK ADD  CONSTRAINT [FK_SfcStep_SfcChart] FOREIGN KEY([ChartId])
REFERENCES [dbo].[SfcChart] ([ChartId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcStep] CHECK CONSTRAINT [FK_SfcStep_SfcChart]
GO
/****** Object:  ForeignKey [FK_SfcStep_SfcStepType]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcStep]  WITH CHECK ADD  CONSTRAINT [FK_SfcStep_SfcStepType] FOREIGN KEY([StepTypeId])
REFERENCES [dbo].[SfcStepType] ([StepTypeId])
GO
ALTER TABLE [dbo].[SfcStep] CHECK CONSTRAINT [FK_SfcStep_SfcStepType]
GO
/****** Object:  ForeignKey [FK_SfcTimeDelayNotification_SfcWindow]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcTimeDelayNotification]  WITH CHECK ADD  CONSTRAINT [FK_SfcTimeDelayNotification_SfcWindow] FOREIGN KEY([windowId])
REFERENCES [dbo].[SfcWindow] ([windowId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcTimeDelayNotification] CHECK CONSTRAINT [FK_SfcTimeDelayNotification_SfcWindow]
GO
/****** Object:  ForeignKey [FK_SfcWindow_SfcControlPanel]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[SfcWindow]  WITH CHECK ADD  CONSTRAINT [FK_SfcWindow_SfcControlPanel] FOREIGN KEY([controlPanelId])
REFERENCES [dbo].[SfcControlPanel] ([ControlPanelId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[SfcWindow] CHECK CONSTRAINT [FK_SfcWindow_SfcControlPanel]
GO
/****** Object:  ForeignKey [FK_TkConsole_TkPost]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkConsole]  WITH CHECK ADD  CONSTRAINT [FK_TkConsole_TkPost] FOREIGN KEY([PostId])
REFERENCES [dbo].[TkPost] ([PostId])
GO
ALTER TABLE [dbo].[TkConsole] CHECK CONSTRAINT [FK_TkConsole_TkPost]
GO
/****** Object:  ForeignKey [FK_TkLogbookDetail_TkLogbook]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkLogbookDetail]  WITH CHECK ADD  CONSTRAINT [FK_TkLogbookDetail_TkLogbook] FOREIGN KEY([LogbookId])
REFERENCES [dbo].[TkLogbook] ([LogbookId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[TkLogbookDetail] CHECK CONSTRAINT [FK_TkLogbookDetail_TkLogbook]
GO
/****** Object:  ForeignKey [FK_TkMessageReply_TkMessageRequest]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkMessageReply]  WITH CHECK ADD  CONSTRAINT [FK_TkMessageReply_TkMessageRequest] FOREIGN KEY([RequestId])
REFERENCES [dbo].[TkMessageRequest] ([RequestId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[TkMessageReply] CHECK CONSTRAINT [FK_TkMessageReply_TkMessageRequest]
GO
/****** Object:  ForeignKey [FK_TkPost_QueueMaster]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkPost]  WITH CHECK ADD  CONSTRAINT [FK_TkPost_QueueMaster] FOREIGN KEY([MessageQueueId])
REFERENCES [dbo].[QueueMaster] ([QueueId])
GO
ALTER TABLE [dbo].[TkPost] CHECK CONSTRAINT [FK_TkPost_QueueMaster]
GO
/****** Object:  ForeignKey [FK_TkPost_TkLogbook]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkPost]  WITH CHECK ADD  CONSTRAINT [FK_TkPost_TkLogbook] FOREIGN KEY([LogbookId])
REFERENCES [dbo].[TkLogbook] ([LogbookId])
GO
ALTER TABLE [dbo].[TkPost] CHECK CONSTRAINT [FK_TkPost_TkLogbook]
GO
/****** Object:  ForeignKey [FK_TkUnit_TkPost]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkUnit]  WITH CHECK ADD  CONSTRAINT [FK_TkUnit_TkPost] FOREIGN KEY([PostId])
REFERENCES [dbo].[TkPost] ([PostId])
GO
ALTER TABLE [dbo].[TkUnit] CHECK CONSTRAINT [FK_TkUnit_TkPost]
GO
/****** Object:  ForeignKey [FK_TkUnitParameterBuffer_TkUnitParameter]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[TkUnitParameterBuffer]  WITH CHECK ADD  CONSTRAINT [FK_TkUnitParameterBuffer_TkUnitParameter] FOREIGN KEY([UnitParameterId])
REFERENCES [dbo].[TkUnitParameter] ([UnitParameterId])
ON UPDATE CASCADE
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[TkUnitParameterBuffer] CHECK CONSTRAINT [FK_TkUnitParameterBuffer_TkUnitParameter]
GO
/****** Object:  ForeignKey [FK_UIRGlineDetails_UIRGline]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[UIRGlineInvolvedProperty]  WITH CHECK ADD  CONSTRAINT [FK_UIRGlineDetails_UIRGline] FOREIGN KEY([UIRId])
REFERENCES [dbo].[UIRGline] ([UIRId])
ON DELETE CASCADE
GO
ALTER TABLE [dbo].[UIRGlineInvolvedProperty] CHECK CONSTRAINT [FK_UIRGlineDetails_UIRGline]
GO
/****** Object:  ForeignKey [FK_UnitAliases_Units]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[UnitAliases]  WITH CHECK ADD  CONSTRAINT [FK_UnitAliases_Units] FOREIGN KEY([name])
REFERENCES [dbo].[Units] ([name])
GO
ALTER TABLE [dbo].[UnitAliases] CHECK CONSTRAINT [FK_UnitAliases_Units]
GO
/****** Object:  ForeignKey [FK_Units_Units]    Script Date: 08/15/2019 14:17:33 ******/
ALTER TABLE [dbo].[Units]  WITH CHECK ADD  CONSTRAINT [FK_Units_Units] FOREIGN KEY([id])
REFERENCES [dbo].[Units] ([id])
GO
ALTER TABLE [dbo].[Units] CHECK CONSTRAINT [FK_Units_Units]
GO