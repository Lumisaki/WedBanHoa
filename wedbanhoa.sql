USE [master]
GO
/****** Object:  Database [wedbanhoa]    Script Date: 6/19/2026 3:54:04 PM ******/
CREATE DATABASE [wedbanhoa]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'wedbanhoa', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\wedbanhoa.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'wedbanhoa_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.MSSQLSERVER\MSSQL\DATA\wedbanhoa_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [wedbanhoa] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [wedbanhoa].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [wedbanhoa] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [wedbanhoa] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [wedbanhoa] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [wedbanhoa] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [wedbanhoa] SET ARITHABORT OFF 
GO
ALTER DATABASE [wedbanhoa] SET AUTO_CLOSE ON 
GO
ALTER DATABASE [wedbanhoa] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [wedbanhoa] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [wedbanhoa] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [wedbanhoa] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [wedbanhoa] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [wedbanhoa] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [wedbanhoa] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [wedbanhoa] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [wedbanhoa] SET  ENABLE_BROKER 
GO
ALTER DATABASE [wedbanhoa] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [wedbanhoa] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [wedbanhoa] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [wedbanhoa] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [wedbanhoa] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [wedbanhoa] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [wedbanhoa] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [wedbanhoa] SET RECOVERY SIMPLE 
GO
ALTER DATABASE [wedbanhoa] SET  MULTI_USER 
GO
ALTER DATABASE [wedbanhoa] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [wedbanhoa] SET DB_CHAINING OFF 
GO
ALTER DATABASE [wedbanhoa] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [wedbanhoa] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [wedbanhoa] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [wedbanhoa] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
ALTER DATABASE [wedbanhoa] SET QUERY_STORE = ON
GO
ALTER DATABASE [wedbanhoa] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [wedbanhoa]
GO
/****** Object:  Table [dbo].[cart]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[cart](
	[cart_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[cart_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[cart_items]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[cart_items](
	[cart_item_id] [int] IDENTITY(1,1) NOT NULL,
	[cart_id] [int] NULL,
	[product_id] [int] NULL,
	[quantity] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[cart_item_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[categories]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[categories](
	[category_id] [int] IDENTITY(1,1) NOT NULL,
	[category_name] [nvarchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[category_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[coupon_usage]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[coupon_usage](
	[usage_id] [int] IDENTITY(1,1) NOT NULL,
	[coupon_id] [int] NOT NULL,
	[user_id] [int] NOT NULL,
	[order_id] [int] NOT NULL,
	[used_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[usage_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[coupons]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[coupons](
	[coupon_id] [int] IDENTITY(1,1) NOT NULL,
	[code] [varchar](50) NOT NULL,
	[discount_percent] [int] NOT NULL,
	[expiry_date] [date] NULL,
	[status] [bit] NULL,
	[min_order_value] [decimal](18, 2) NULL,
PRIMARY KEY CLUSTERED 
(
	[coupon_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[order_details]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[order_details](
	[detail_id] [int] IDENTITY(1,1) NOT NULL,
	[order_id] [int] NULL,
	[product_id] [int] NULL,
	[quantity] [int] NULL,
	[price] [decimal](10, 2) NULL,
PRIMARY KEY CLUSTERED 
(
	[detail_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[orders]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[orders](
	[order_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NULL,
	[total_price] [decimal](10, 2) NULL,
	[status] [nvarchar](50) NULL,
	[order_date] [datetime] NULL,
	[sender_name] [nvarchar](255) NULL,
	[sender_phone] [varchar](20) NULL,
	[sender_email] [varchar](255) NULL,
	[receiver_name] [nvarchar](255) NULL,
	[receiver_phone] [varchar](20) NULL,
	[receiver_address] [nvarchar](500) NULL,
	[receiver_city] [nvarchar](150) NULL,
	[receiver_district] [nvarchar](150) NULL,
	[delivery_date] [varchar](50) NULL,
	[delivery_time] [nvarchar](100) NULL,
	[card_message] [nvarchar](1000) NULL,
	[vat_amount] [decimal](18, 2) NULL,
	[special_requests] [nvarchar](1000) NULL,
	[payment_method] [nvarchar](100) NULL,
	[coupon_code] [varchar](50) NULL,
	[discount_amount] [decimal](18, 2) NULL,
PRIMARY KEY CLUSTERED 
(
	[order_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[products]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[products](
	[product_id] [int] IDENTITY(1,1) NOT NULL,
	[product_name] [nvarchar](200) NOT NULL,
	[description] [nvarchar](max) NULL,
	[price] [decimal](10, 2) NOT NULL,
	[stock] [int] NULL,
	[image] [varchar](255) NULL,
	[category_id] [int] NULL,
	[created_at] [datetime] NULL,
	[discount] [int] NULL,
PRIMARY KEY CLUSTERED 
(
	[product_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[reviews]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[reviews](
	[review_id] [int] IDENTITY(1,1) NOT NULL,
	[user_id] [int] NULL,
	[product_id] [int] NULL,
	[rating] [int] NULL,
	[comment] [nvarchar](max) NULL,
	[review_date] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[review_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[roles]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[roles](
	[role_id] [int] NOT NULL,
	[role_name] [nvarchar](50) NULL,
PRIMARY KEY CLUSTERED 
(
	[role_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[support_messages]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[support_messages](
	[msg_id] [int] IDENTITY(1,1) NOT NULL,
	[client_id] [int] NOT NULL,
	[client_email] [varchar](100) NULL,
	[sender_role] [varchar](20) NULL,
	[message_text] [nvarchar](max) NULL,
	[created_at] [datetime] NULL,
	[is_read] [bit] NULL,
PRIMARY KEY CLUSTERED 
(
	[msg_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
/****** Object:  Table [dbo].[users]    Script Date: 6/19/2026 3:54:05 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[users](
	[user_id] [int] IDENTITY(1,1) NOT NULL,
	[fullname] [nvarchar](100) NOT NULL,
	[email] [varchar](100) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[phone] [varchar](20) NULL,
	[address] [nvarchar](255) NULL,
	[security_question] [nvarchar](255) NULL,
	[security_answer] [nvarchar](255) NULL,
	[role_id] [int] NULL,
	[created_at] [datetime] NULL,
PRIMARY KEY CLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[cart] ON 

INSERT [dbo].[cart] ([cart_id], [user_id]) VALUES (2, 1)
INSERT [dbo].[cart] ([cart_id], [user_id]) VALUES (3, 2)
INSERT [dbo].[cart] ([cart_id], [user_id]) VALUES (1, 3)
INSERT [dbo].[cart] ([cart_id], [user_id]) VALUES (4, 6)
INSERT [dbo].[cart] ([cart_id], [user_id]) VALUES (5, 11)
SET IDENTITY_INSERT [dbo].[cart] OFF
GO
SET IDENTITY_INSERT [dbo].[cart_items] ON 

INSERT [dbo].[cart_items] ([cart_item_id], [cart_id], [product_id], [quantity]) VALUES (20, 2, 1, 8)
SET IDENTITY_INSERT [dbo].[cart_items] OFF
GO
SET IDENTITY_INSERT [dbo].[categories] ON 

INSERT [dbo].[categories] ([category_id], [category_name]) VALUES (1, N'Hoa Sinh Nhật')
INSERT [dbo].[categories] ([category_id], [category_name]) VALUES (2, N'Hoa Tình Yêu')
INSERT [dbo].[categories] ([category_id], [category_name]) VALUES (3, N'Hoa Đám Cưới')
INSERT [dbo].[categories] ([category_id], [category_name]) VALUES (4, N'Hoa Khai Trương')
INSERT [dbo].[categories] ([category_id], [category_name]) VALUES (5, N'Hoa Tang Lễ')
INSERT [dbo].[categories] ([category_id], [category_name]) VALUES (6, N'Chậu Hoa Cảnh')
SET IDENTITY_INSERT [dbo].[categories] OFF
GO
SET IDENTITY_INSERT [dbo].[coupon_usage] ON 

INSERT [dbo].[coupon_usage] ([usage_id], [coupon_id], [user_id], [order_id], [used_at]) VALUES (1, 2, 6, 66, CAST(N'2026-05-21T09:30:53.247' AS DateTime))
INSERT [dbo].[coupon_usage] ([usage_id], [coupon_id], [user_id], [order_id], [used_at]) VALUES (2, 1, 6, 66, CAST(N'2026-05-21T09:30:53.427' AS DateTime))
INSERT [dbo].[coupon_usage] ([usage_id], [coupon_id], [user_id], [order_id], [used_at]) VALUES (3, 2, 3, 67, CAST(N'2026-05-21T09:38:39.470' AS DateTime))
SET IDENTITY_INSERT [dbo].[coupon_usage] OFF
GO
SET IDENTITY_INSERT [dbo].[coupons] ON 

INSERT [dbo].[coupons] ([coupon_id], [code], [discount_percent], [expiry_date], [status], [min_order_value]) VALUES (1, N'BAHOA10', 10, CAST(N'2026-12-31' AS Date), 1, CAST(1.00 AS Decimal(18, 2)))
INSERT [dbo].[coupons] ([coupon_id], [code], [discount_percent], [expiry_date], [status], [min_order_value]) VALUES (2, N'FLOWER30', 30, CAST(N'2026-12-31' AS Date), 1, CAST(1.00 AS Decimal(18, 2)))
INSERT [dbo].[coupons] ([coupon_id], [code], [discount_percent], [expiry_date], [status], [min_order_value]) VALUES (3, N'VIP3M-63629', 10, NULL, 1, CAST(1.00 AS Decimal(18, 2)))
SET IDENTITY_INSERT [dbo].[coupons] OFF
GO
SET IDENTITY_INSERT [dbo].[order_details] ON 

INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (1, 2, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (2, 2, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (3, 3, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (4, 4, 3, 1, CAST(603000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (5, 5, 3, 1, CAST(603000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (6, 6, 3, 1, CAST(603000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (7, 7, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (8, 7, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (9, 8, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (10, 8, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (11, 9, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (12, 9, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (13, 10, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (14, 10, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (15, 11, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (16, 11, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (39, 25, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (40, 26, 4, 1, CAST(800000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (41, 27, 4, 1, CAST(800000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (42, 28, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (43, 29, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (44, 30, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (45, 31, 2, 1, CAST(342000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (46, 31, 6, 1, CAST(854400.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (47, 32, 8, 1, CAST(700000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (49, 34, 14, 1, CAST(190000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (73, 59, 8, 1, CAST(855000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (75, 61, 8, 1, CAST(855000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (76, 62, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (77, 62, 3, 1, CAST(603000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (78, 62, 4, 1, CAST(800000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (79, 62, 6, 1, CAST(864000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (80, 62, 8, 1, CAST(855000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (81, 64, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (82, 65, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (83, 66, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
INSERT [dbo].[order_details] ([detail_id], [order_id], [product_id], [quantity], [price]) VALUES (84, 67, 1, 1, CAST(500000.00 AS Decimal(10, 2)))
SET IDENTITY_INSERT [dbo].[order_details] OFF
GO
SET IDENTITY_INSERT [dbo].[orders] ON 

INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (1, 3, CAST(486000.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T13:40:08.463' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000028', N'hoan@gmail.com', N'Người đó', N'0935235380', N'36', N'Hà Nội', N'Đan phượng', N'2026-05-28', N'Từ 07:00 - 09:00', N'yêu em', CAST(36000.00 AS Decimal(18, 2)), N'giao tận tay', N'Chuyển khoản qua QR Code Online', N'BAHOA10', CAST(50000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (2, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-17T14:21:20.737' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'số 23', N'Hà Nội', N'tam hiệp', N'2026-05-26', N'Từ 07:00 - 09:00', N'oek', CAST(95712.00 AS Decimal(18, 2)), N'cố lên', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (3, 3, CAST(922752.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T14:23:10.543' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'FPT', N'0935235345', N'36', N'Hà Nội', N'Đan phượng', N'2026-05-28', N'Từ 07:00 - 09:00', N'FPT', CAST(68352.00 AS Decimal(18, 2)), N'FPT', N'Chuyển khoản qua QR Code Online', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (4, 3, CAST(651240.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T14:35:06.537' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'lumi', N'0935235367', N'35', N'Hà Nội', N'Tam Hiệp', N'2026-05-27', N'Từ 07:00 - 09:00', N'oke lumi', CAST(48240.00 AS Decimal(18, 2)), N'oke', N'Chuyển khoản qua QR Code Online', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (5, 3, CAST(651240.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T14:57:00.917' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'lumi', N'0935235367', N'35', N'Hà Nội', N'Tam Hiệp', N'2026-05-27', N'Từ 07:00 - 09:00', N'oke lumi', CAST(48240.00 AS Decimal(18, 2)), N'oke', N'Chưa chọn', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (6, 3, CAST(651240.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T15:13:48.030' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'lumi', N'0935235367', N'35', N'Hà Nội', N'Tam Hiệp', N'2026-05-27', N'Từ 07:00 - 09:00', N'oke lumi', CAST(48240.00 AS Decimal(18, 2)), N'oke', N'Chưa chọn', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (7, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T15:17:27.053' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'số 23', N'Hà Nội', N'tam hiệp', N'2026-05-26', N'Từ 07:00 - 09:00', N'oek', CAST(95712.00 AS Decimal(18, 2)), N'cố lên', N'Chưa chọn', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (8, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-17T15:39:56.660' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'số 23', N'Hà Nội', N'tam hiệp', N'2026-05-26', N'Từ 07:00 - 09:00', N'oek', CAST(95712.00 AS Decimal(18, 2)), N'cố lên', N'Chưa chọn', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (9, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-17T15:46:49.650' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'số 23', N'Hà Nội', N'tam hiệp', N'2026-05-26', N'Từ 07:00 - 09:00', N'oek', CAST(95712.00 AS Decimal(18, 2)), N'cố lên', N'Chuyển khoản qua QR Code Online', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (10, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-18T10:27:35.893' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Phúc Thọ', N'', N'', N'2026-05-27', N'Giao nhanh hỏa tốc (60 phút)', N'', CAST(95712.00 AS Decimal(18, 2)), N'', N'Chưa chọn', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (11, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-18T11:08:18.260' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Phúc Thọ', N'', N'', N'2026-05-27', N'Giao nhanh hỏa tốc (60 phút)', N'', CAST(95712.00 AS Decimal(18, 2)), N'', N'Chuyển khoản qua QR Code Online', N'', CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (25, 3, CAST(486000.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-18T15:14:14.943' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Liên Hiệp', N'Hà Nội', N'ĐỖ SỸ HOÀN ', N'2026-05-27', N'Từ 07:00 - 09:00', N'ảwfaga', CAST(36000.00 AS Decimal(18, 2)), N'gshrwyrjtrrs', N'Chưa chọn', N'BAHOA10', CAST(50000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (26, 3, CAST(777600.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-18T15:36:40.713' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Liên Hiệp', N'Hà Nội', N'Liên Hiệp', N'2026-05-23', N'Từ 07:00 - 09:00', N'oke', CAST(57600.00 AS Decimal(18, 2)), N'oke', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'BAHOA10', CAST(80000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (27, 3, CAST(777600.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-18T15:38:38.707' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Liên Hiệp', N'Hà Nội', N'Liên Hiệp', N'2026-05-23', N'Từ 07:00 - 09:00', N'oke', CAST(57600.00 AS Decimal(18, 2)), N'oke', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'BAHOA10', CAST(80000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (28, 3, CAST(540000.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-18T15:53:28.137' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Liên Hiệp', N'Hà Nội', N'Liên Hiệp', N'2026-05-23', N'Từ 07:00 - 09:00', N'oke', CAST(40000.00 AS Decimal(18, 2)), N'oke', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (29, 3, CAST(540000.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-18T15:55:58.027' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Liên Hiệp', N'Hà Nội', N'Liên Hiệp', N'2026-05-23', N'Từ 07:00 - 09:00', N'oke', CAST(40000.00 AS Decimal(18, 2)), N'oke', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (30, 3, CAST(540000.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-18T15:59:13.593' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Liên Hiệp', N'Hà Nội', N'Liên Hiệp', N'2026-05-23', N'Từ 07:00 - 09:00', N'oke', CAST(40000.00 AS Decimal(18, 2)), N'oke', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (31, 3, CAST(1292112.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-18T16:57:21.873' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'Phúc Thọ', N'', N'', N'2026-05-27', N'Giao nhanh hỏa tốc (60 phút)', N'', CAST(95712.00 AS Decimal(18, 2)), N'', N'Chuyển khoản qua QR Code Online', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (32, 3, CAST(756000.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-18T19:28:47.947' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Lumi', N'0396580099', N'Hòa Lạc', N'Hà Nội', N'Thạch Thất', N'2026-05-29', N'Từ 07:00 - 09:00', N'lumi', CAST(56000.00 AS Decimal(18, 2)), N'oke okr', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (34, 11, CAST(205200.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-19T20:15:00.580' AS DateTime), N'Sỹ Hoàn', N'0900004795', N'hoan1@gmail.com', N'Sỹ Hoàn', N'0900004795', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-28', N'Từ 13:00 - 16:00', N'quá oke', CAST(15200.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (59, 11, CAST(923400.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-20T08:06:08.610' AS DateTime), N'Sỹ Hoàn', N'0900000029', N'hoan1@gmail.com', N'Sỹ Hoàn', N'0900000029', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(68400.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (61, 11, CAST(923400.00 AS Decimal(10, 2)), N'Unpaid', CAST(N'2026-05-20T09:03:07.723' AS DateTime), N'Sỹ Hoàn', N'0900000029', N'hoan1@gmail.com', N'Sỹ Hoàn', N'0900000029', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(68400.00 AS Decimal(18, 2)), N'', N'Chưa chọn', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (62, 11, CAST(3911760.00 AS Decimal(10, 2)), N'Cancelled', CAST(N'2026-05-20T21:20:35.547' AS DateTime), N'Sỹ Hoàn', N'0900000029', N'hoan1@gmail.com', N'Sỹ Hoàn', N'0900000029', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(289760.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', NULL, CAST(0.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (64, 11, CAST(378000.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-21T09:08:47.150' AS DateTime), N'Sỹ Hoàn', N'0900000029', N'hoan1@gmail.com', N'Sỹ Hoàn', N'0900000029', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(28000.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'FLOWER30', CAST(150000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (65, 11, CAST(378000.00 AS Decimal(10, 2)), N'Unpaid', CAST(N'2026-05-21T09:10:14.133' AS DateTime), N'Sỹ Hoàn', N'0900000029', N'hoan1@gmail.com', N'Sỹ Hoàn', N'0900000029', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(28000.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'FLOWER30', CAST(150000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (66, 6, CAST(324000.00 AS Decimal(10, 2)), N'Completed', CAST(N'2026-05-21T09:30:53.147' AS DateTime), N'Đỗ Hoàn', N'0900000346', N'hoan2@gmail.com', N'Đỗ Hoàn', N'0900000346', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(24000.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'FLOWER30,BAHOA10', CAST(200000.00 AS Decimal(18, 2)))
INSERT [dbo].[orders] ([order_id], [user_id], [total_price], [status], [order_date], [sender_name], [sender_phone], [sender_email], [receiver_name], [receiver_phone], [receiver_address], [receiver_city], [receiver_district], [delivery_date], [delivery_time], [card_message], [vat_amount], [special_requests], [payment_method], [coupon_code], [discount_amount]) VALUES (67, 3, CAST(378000.00 AS Decimal(10, 2)), N'Pending', CAST(N'2026-05-21T09:38:39.350' AS DateTime), N'Đỗ Sỹ Hoàn', N'0900000029', N'hoan@gmail.com', N'Đỗ Sỹ Hoàn', N'0900000029', N'45', N'Hà Nội', N'Phúc Thọ', N'2026-05-27', N'Từ 07:00 - 09:00', N'', CAST(28000.00 AS Decimal(18, 2)), N'', N'Thanh toán tiền mặt trực tiếp khi nhận hoa (COD)', N'FLOWER30', CAST(150000.00 AS Decimal(18, 2)))
SET IDENTITY_INSERT [dbo].[orders] OFF
GO
SET IDENTITY_INSERT [dbo].[products] ON 

INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (1, N'Hoa Hồng Siêu Đẹp', N'Hoa rất đẹp và đỏ', CAST(500000.00 AS Decimal(10, 2)), 45, N'hoa.png', 2, CAST(N'2026-05-17T13:29:56.650' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (2, N'Hoa Hồng Trắng Nhạt', N'Rất Trắng', CAST(360000.00 AS Decimal(10, 2)), 39, N'hoa1.jpg', 2, CAST(N'2026-05-17T13:29:56.650' AS DateTime), 5)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (3, N'Hoa Sang Chảnh', N'Chảnh', CAST(670000.00 AS Decimal(10, 2)), 8, N'hoa2.jpg', 1, CAST(N'2026-05-17T13:29:56.650' AS DateTime), 10)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (4, N'Hoa Hồng Trái Tim', N'Trái Tim Đỏ', CAST(800000.00 AS Decimal(10, 2)), 14, N'hoa3.jpg', 3, CAST(N'2026-05-17T13:29:56.650' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (5, N'Hoa Tang', N'Chia Buồn', CAST(700000.00 AS Decimal(10, 2)), 20, N'hoa5.jpg', 5, CAST(N'2026-05-17T13:29:56.650' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (6, N'Hoa Khai Trương', N'Happy', CAST(900000.00 AS Decimal(10, 2)), 35, N'hoa6.jpg', 4, CAST(N'2026-05-17T13:38:34.977' AS DateTime), 4)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (8, N'Hoa Tulip Hồng', N'Đệp lắm', CAST(900000.00 AS Decimal(10, 2)), 98, N'hoa7.jpg', 2, CAST(N'2026-05-18T19:27:18.097' AS DateTime), 5)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (14, N'Hoa Xuyến Chi', N'Hoa rất đẹp', CAST(200000.00 AS Decimal(10, 2)), 99, N'hoa9.jpg', 2, CAST(N'2026-05-18T20:56:17.243' AS DateTime), 5)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (15, N'Hồng phấn phối Cẩm chướng', N'Tượng trưng cho tình yêu ngọt ngào, lòng biết ơn sâu sắc và sự ngưỡng mộ chân thành.', CAST(400000.00 AS Decimal(10, 2)), 10, N'hoa11.webp', 1, CAST(N'2026-05-21T18:28:43.943' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (16, N'Đồng tiền rực rỡ phối Hoa hồng', N'Bó hoa tỏa năng lượng rực rỡ với sự góp mặt của hoa đồng tiền rạng ngời đan xen cùng hoa hồng quyến rũ. Không chỉ sở hữu vẻ đẹp tươi tắn, bó hoa còn là lời chúc tuyệt vời về sự may mắn, tài lộc và hạnh phúc viên mãn. Rất phù hợp để bạn gửi tặng nhân dịp khai trương, tốt nghiệp hoặc chúc mừng sinh nhật tuổi mới!', CAST(450000.00 AS Decimal(10, 2)), 10, N'hoa12.webp', 1, CAST(N'2026-05-21T18:29:54.383' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (17, N'Ly hồng sang trọng', N'Hoa ly hồng luôn tỏa ra một sức hút kỳ lạ với hương thơm quyến rũ và những cánh hoa vươn dài đầy kiêu hãnh. Bó hoa là biểu tượng của sự đẳng cấp và quý phái, gửi gắm trọn vẹn lời chúc về sự thịnh vượng. Đây là lựa chọn hoàn hảo để dành tặng đối tác, sếp nữ hoặc những người phụ nữ thanh lịch mà bạn vô cùng kính trọng.', CAST(470000.00 AS Decimal(10, 2)), 10, N'hoa13.webp', 1, CAST(N'2026-05-21T18:30:21.373' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (18, N'Cẩm tú cầu & Cúc họa mi', N'Một thiết kế mang đậm chất thơ và sự dịu dàng! Khối cầu viên mãn của cẩm tú cầu làm nền hoàn hảo cho những bông cúc họa mi nhỏ nhắn, mộc mạc tỏa sáng. Bó hoa mang lại cảm giác bình yên, trong trẻo, thích hợp để xoa dịu tâm hồn và gửi gắm sự quan tâm tinh tế đến một nửa yêu thương của bạn.', CAST(478000.00 AS Decimal(10, 2)), 10, N'hoa14.webp', 1, CAST(N'2026-05-21T18:30:45.010' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (19, N'Hồng phấn nhạt', N'Không cần rực rỡ phô trương, bó hồng pastel vẫn đủ sức chinh phục mọi ánh nhìn bằng sự ngọt ngào, e ấp và vô cùng duyên dáng. Lớp giấy gói cao cấp ton-sur-ton càng làm tôn lên vẻ đẹp đài các của những nụ hồng. Bó hoa là món quà "must-have" trong ngày hẹn hò đầu tiên hoặc những dịp kỷ niệm lãng mạn.', CAST(367000.00 AS Decimal(10, 2)), 10, N'hoa15.webp', 2, CAST(N'2026-05-21T18:31:20.983' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (20, N'Hồng cam đào ấm áp', N'Không cần rực rỡ phô trương, bó hồng pastel vẫn đủ sức chinh phục mọi ánh nhìn bằng sự ngọt ngào, e ấp và vô cùng duyên dáng. Lớp giấy gói cao cấp ton-sur-ton càng làm tôn lên vẻ đẹp đài các của những nụ hồng. Bó hoa là món quà "must-have" trong ngày hẹn hò đầu tiên hoặc những dịp kỷ niệm lãng mạn.', CAST(570000.00 AS Decimal(10, 2)), 10, N'hoa16.webp', 2, CAST(N'2026-05-21T18:31:47.660' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (21, N'Hồng màu trà cổ điển', N'Tỏa sáng như những tia nắng ban mai, bó hồng vàng mang trong mình nguồn năng lượng tích cực và sự lạc quan tuyệt đối. Đây là món quà hoàn hảo để tôn vinh một tình bạn đẹp, chúc mừng đồng nghiệp thăng tiến hoặc khích lệ tinh thần người thương yêu vượt qua những thử thách mới.', CAST(360000.00 AS Decimal(10, 2)), 10, N'hoa17.webp', 1, CAST(N'2026-05-21T18:32:21.230' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (22, N'Cát tường bình an', N'Đúng như tên gọi "Cát Tường" (điều may mắn), bó hoa là lời chúc vạn sự hanh thông, bình an và thuận lợi. Những cánh hoa mỏng manh, xếp lớp tinh tế gợi lên vẻ đẹp e ấp, dịu dàng. Lựa chọn tuyệt vời và đầy ý nghĩa để chúc mừng tuổi mới, tân gia hoặc ngày Quốc tế Phụ nữ.', CAST(346300.00 AS Decimal(10, 2)), 10, N'hoa18.webp', 1, CAST(N'2026-05-21T18:32:44.370' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (23, N'Hồng cam đào ấm áp', N'Sắc cam đào mang đến cảm giác vô cùng ấm áp, gần gũi nhưng không kém phần sang trọng. Bó hoa là đại sứ tuyệt vời để truyền tải lòng biết ơn, sự trân trọng và những tình cảm chân thành nhất từ đáy lòng. Cực kỳ phù hợp để tặng cho đồng nghiệp, bạn thân hoặc những người đã luôn đồng hành, giúp đỡ bạn.', CAST(200000.00 AS Decimal(10, 2)), 10, N'hoa19.webp', 3, CAST(N'2026-05-21T18:33:09.790' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (24, N'Hoa cẩm chướng + hồng pastel', N'sự ngọt ngào, biết ơn và yêu thương dịu dàng.

Phù hợp tặng mẹ, cô giáo hoặc người thân.', CAST(278000.00 AS Decimal(10, 2)), 10, N'hoa20.webp', 3, CAST(N'2026-05-21T18:37:31.943' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (25, N'Hoa hồng vàng', N'tình bạn, niềm vui, sự lạc quan và thành công.

Cũng thể hiện lời chúc tốt đẹp, năng lượng tích cực.', CAST(540000.00 AS Decimal(10, 2)), 10, N'hoa21.webp', 2, CAST(N'2026-05-21T18:38:06.570' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (26, N'Hoa cẩm chướng', N'sự ngọt ngào, biết ơn và yêu thương dịu dàng.

Phù hợp tặng mẹ, cô giáo hoặc người thân.', CAST(300000.00 AS Decimal(10, 2)), 10, N'hoa22.webp', 3, CAST(N'2026-05-21T18:39:15.210' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (27, N'Hoa sen', N'may mắn, bình an, nhẹ nhàng và mang lại cảm giác chữa lành.

Màu xanh lá còn tượng trưng cho hy vọng và sự khởi đầu mới.', CAST(180000.00 AS Decimal(10, 2)), 10, N'hoa23.webp', 1, CAST(N'2026-05-21T18:39:54.533' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (28, N'Hoa cúc mẫu đơn xanh', N'độc đáo, sáng tạo, hy vọng và sự quan tâm.

Tông xanh tạo cảm giác nổi bật và cá tính.', CAST(480000.00 AS Decimal(10, 2)), 10, N'hoa24.webp', 1, CAST(N'2026-05-21T18:40:44.100' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (29, N'Hoa baby tím', N'tình yêu thủy chung, lãng mạn và nhớ thương.

Hoa baby tượng trưng cho tình cảm bền lâu.', CAST(570000.00 AS Decimal(10, 2)), 10, N'hoa25.webp', 2, CAST(N'2026-05-21T18:41:05.293' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (30, N'Hoa baby tím + hồng đỏ', N'tình yêu thủy chung, lãng mạn và nhớ thương.

Hoa baby tượng trưng cho tình cảm bền lâu.', CAST(755000.00 AS Decimal(10, 2)), 10, N'hoa26.webp', 2, CAST(N'2026-05-21T18:41:31.630' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (31, N'Hoa hồng xanh dương', N'bí ẩn, độc nhất, điều kỳ diệu và những điều khó đạt được.

Thường dành cho người đặc biệt, cá tính.', CAST(370000.00 AS Decimal(10, 2)), 10, N'hoa27.webp', 2, CAST(N'2026-05-21T18:41:51.320' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (32, N'Hoa cát tường xanh lá', N'may mắn, bình an, nhẹ nhàng và mang lại cảm giác chữa lành.

Màu xanh lá còn tượng trưng cho hy vọng và sự khởi đầu mới.', CAST(540000.00 AS Decimal(10, 2)), 10, N'hoa28.webp', 1, CAST(N'2026-05-21T18:42:10.890' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (33, N'Hoa lan hồ điệp xanh + cẩm tú cầu xanh', N'sang trọng, quý phái, thành công và điều kỳ diệu.

Màu xanh tượng trưng cho sự bình yên và hy vọng.', CAST(430000.00 AS Decimal(10, 2)), 10, N'hoa29.webp', 4, CAST(N'2026-05-21T18:43:27.537' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (34, N'Hoa lavender (oải hương)', N'chung thủy, chờ đợi và chữa lành.

Hương thơm giúp thư giãn, tạo cảm giác nhẹ nhàng.', CAST(370000.00 AS Decimal(10, 2)), 10, N'hoa30.webp', 2, CAST(N'2026-05-21T18:43:55.690' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (35, N'Hoa đồng tiền cánh nhện trắng', N'may mắn, vui vẻ, tinh khiết và tích cực.

Kiểu cánh nhện còn thể hiện cá tính và độc đáo.', CAST(590000.00 AS Decimal(10, 2)), 10, N'hoa31.webp', 1, CAST(N'2026-05-21T18:44:23.307' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (36, N'Hoa cúc ping pong tạo hình gấu trúc', N'dễ thương, vui vẻ, mang lại tiếng cười và sự quan tâm.

Thường dùng để tặng bạn bè hoặc người yêu trẻ trung.', CAST(500000.00 AS Decimal(10, 2)), 10, N'hoa32.webp', 2, CAST(N'2026-05-21T18:44:44.213' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (37, N'Kệ hoa tone xanh dương khai trương', N'thành công, phát triển, thuận lợi và thịnh vượng.

Tone xanh tạo cảm giác hiện đại và nổi bật.', CAST(590000.00 AS Decimal(10, 2)), 10, N'hoa33.webp', 4, CAST(N'2026-05-21T18:45:09.170' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (38, N'Kệ hoa lan hồ điệp trắng + hồng đỏ', N'sang trọng, phát đạt, may mắn và lời chúc thành công lớn.

Lan hồ điệp tượng trưng cho tài lộc và sự viên mãn.', CAST(670000.00 AS Decimal(10, 2)), 10, N'hoa34.webp', 4, CAST(N'2026-05-21T18:45:29.023' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (39, N'Kệ hoa hồng môn đỏ + lan trắng', N'Ý nghĩa: nhiệt huyết, quyền lực, may mắn và thăng tiến.

Hồng môn đỏ thường mang ý nghĩa phát tài phát lộc.', CAST(876000.00 AS Decimal(10, 2)), 10, N'hoa35.webp', 4, CAST(N'2026-05-21T18:45:57.510' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (40, N'Kệ hoa chúc mừng tone hồng trắng', N'yêu thương, hạnh phúc, sự tinh tế và lời chúc tốt đẹp.

Thích hợp khai trương, sinh nhật hoặc kỷ niệm.', CAST(780000.00 AS Decimal(10, 2)), 10, N'hoa36.webp', 4, CAST(N'2026-05-21T18:46:16.770' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (41, N'Vòng hoa tang trắng', N'tưởng nhớ, chia buồn, tiễn biệt và sự kính trọng dành cho người đã khuất.

Hoa trắng tượng trưng cho sự thanh thản và tiếc thương.', CAST(680000.00 AS Decimal(10, 2)), 10, N'hoa37.webp', 5, CAST(N'2026-05-21T18:46:38.287' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (42, N'Vòng hoa tang cúc trắng', N'chia buồn, tưởng nhớ và tiễn biệt người đã khuất.

Hoa cúc trắng tượng trưng cho sự kính trọng và tiếc thương sâu sắc.', CAST(700000.00 AS Decimal(10, 2)), 10, N'hoa38.webp', 5, CAST(N'2026-05-21T18:47:44.500' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (43, N'Vòng hoa lan tím', N'tưởng niệm trang trọng, sự quý mến và lòng thành kính.

Lan tím còn thể hiện sự thủy chung và cao quý.', CAST(980000.00 AS Decimal(10, 2)), 10, N'hoa39.webp', 5, CAST(N'2026-05-21T18:48:07.830' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (44, N'Vòng hoa lan trắng + hồng môn xanh', N'thanh khiết, tiễn đưa nhẹ nhàng và lòng biết ơn.

Thường dùng trong lễ viếng trang nghiêm.', CAST(1250000.00 AS Decimal(10, 2)), 10, N'hoa40.webp', 4, CAST(N'2026-05-21T18:48:26.333' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (45, N'Vòng hoa tím hồng', N'thanh khiết, tiễn đưa nhẹ nhàng và lòng biết ơn.

Thường dùng trong lễ viếng trang nghiêm.', CAST(900000.00 AS Decimal(10, 2)), 10, N'hoa41.webp', 4, CAST(N'2026-05-21T18:49:07.917' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (46, N'Chậu lan hồ điệp tím', N'giàu sang, phú quý, quyền lực và may mắn.

Rất phổ biến làm quà tặng dịp khai trương, Tết.', CAST(450000.00 AS Decimal(10, 2)), 10, N'hoa42.webp', 6, CAST(N'2026-05-21T18:49:30.790' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (47, N'Chậu lan hồ điệp vàng', N'tài lộc, thịnh vượng, niềm vui và thành công.

Màu vàng tượng trưng cho tiền tài và phát đạt.', CAST(780000.00 AS Decimal(10, 2)), 10, N'hoa43.webp', 6, CAST(N'2026-05-21T18:49:48.877' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (48, N'Lan hồ điệp mini hồng nhạt', N'ngọt ngào, nhẹ nhàng, hạnh phúc và tình cảm chân thành.

Phù hợp tặng người yêu hoặc trang trí sang trọng.', CAST(430000.00 AS Decimal(10, 2)), 10, N'hoa44.webp', 6, CAST(N'2026-05-21T18:50:08.640' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (49, N'Lan hồ điệp trắng', N'thuần khiết, thanh cao và bình an.

Thường dùng làm quà tặng trang nhã.', CAST(400000.00 AS Decimal(10, 2)), 10, N'hoa45.webp', 6, CAST(N'2026-05-21T18:50:28.050' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (50, N'Lan hồ điệp tím nghệ thuật', N'sang trọng, sáng tạo và đẳng cấp.

Dáng uốn nghệ thuật tượng trưng cho sự thăng tiến.', CAST(200000.00 AS Decimal(10, 2)), 10, N'hoa46.webp', 6, CAST(N'2026-05-21T18:50:45.713' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (51, N'Tiểu cảnh lan hồ điệp vàng lớn', N'sung túc, phát tài, trường thọ và viên mãn.

Thường trưng bày dịp Tết hoặc khai trương lớn.', CAST(150000.00 AS Decimal(10, 2)), 10, N'hoa48.webp', 6, CAST(N'2026-05-21T18:51:07.413' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (52, N'Cây bonsai dát vàng', N'tài lộc, phú quý, quyền lực và thịnh vượng.

Thường dùng trang trí hoặc làm quà phong thủy cao cấp.', CAST(9999999.00 AS Decimal(10, 2)), 10, N'hoa60.jpg', 6, CAST(N'2026-05-21T18:59:58.490' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (53, N'Hoa giấy bonsai hồng', N'giản dị, bền bỉ, hạnh phúc gia đình và sự gắn kết.

Hoa giấy còn tượng trưng cho sức sống mạnh mẽ.', CAST(3000000.00 AS Decimal(10, 2)), 10, N'hoa49.jpg', 6, CAST(N'2026-05-21T19:00:21.817' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (54, N'Cây sam hương / linh sam bonsai có quả đỏ', N'trường thọ, may mắn và sung túc.

Quả đỏ tượng trưng cho tài lộc và niềm vui.', CAST(700000.00 AS Decimal(10, 2)), 10, N'hoa52.jpg', 6, CAST(N'2026-05-21T19:00:54.513' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (55, N'Mai đỏ bonsai', N'may mắn, hạnh phúc và khởi đầu tốt đẹp.

Rất được ưa chuộng dịp Tết.', CAST(240000.00 AS Decimal(10, 2)), 10, N'hoa51.jpg', 6, CAST(N'2026-05-21T19:01:23.763' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (56, N'Cây bonsai quả đỏ mini', N': tài lộc, sum vầy và sung túc.

Dáng cong biểu tượng cho sự mềm mại, thuận lợi.', CAST(500000.00 AS Decimal(10, 2)), 10, N'hoa50.jpg', 6, CAST(N'2026-05-21T19:02:01.053' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (57, N'Hoa giấy bonsai tím', N'thủy chung, lãng mạn và bình yên.

Dáng bonsai cổ thể hiện sự kiên cường theo thời gian.', CAST(680000.00 AS Decimal(10, 2)), 10, N'hoa53.jpg', 6, CAST(N'2026-05-21T19:02:23.210' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (58, N'Cây tùng bonsai mini', N'sức sống bền bỉ, trường thọ và khí chất mạnh mẽ.

Trong phong thủy tượng trưng cho sự vững vàng.', CAST(199999.00 AS Decimal(10, 2)), 10, N'hoa55.jpg', 6, CAST(N'2026-05-21T19:02:54.300' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (59, N'Hoa anh đào bonsai mini', N'vẻ đẹp mong manh, tuổi trẻ và khoảnh khắc quý giá.

Mang đậm phong cách Nhật Bản.', CAST(1200000.00 AS Decimal(10, 2)), 10, N'hoa57.jpg', 6, CAST(N'2026-05-21T19:03:27.307' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (60, N'Hoa sứ Thái bonsai', N'may mắn, tài lộc và vẻ đẹp quyến rũ.

Thân cây độc đáo tượng trưng cho sức sống mạnh.', CAST(500000.00 AS Decimal(10, 2)), 10, N'hoa58.jpg', 6, CAST(N'2026-05-21T19:04:02.987' AS DateTime), 0)
INSERT [dbo].[products] ([product_id], [product_name], [description], [price], [stock], [image], [category_id], [created_at], [discount]) VALUES (61, N'Hoa ngũ sắc bonsai (trâm ổi)', N'niềm vui, sự đa dạng và năng lượng tích cực.

Hoa nhiều màu tượng trưng cho cuộc sống phong phú, đủ đầy.', CAST(900000.00 AS Decimal(10, 2)), 10, N'hoa59.jpg', 6, CAST(N'2026-05-21T19:04:26.827' AS DateTime), 0)
SET IDENTITY_INSERT [dbo].[products] OFF
GO
SET IDENTITY_INSERT [dbo].[reviews] ON 

INSERT [dbo].[reviews] ([review_id], [user_id], [product_id], [rating], [comment], [review_date]) VALUES (1, 2, 1, 5, N'Very beautiful flowers', CAST(N'2026-05-17T13:29:56.663' AS DateTime))
INSERT [dbo].[reviews] ([review_id], [user_id], [product_id], [rating], [comment], [review_date]) VALUES (2, 3, 8, 5, N'Hoa quá đẹp luôn á shop', CAST(N'2026-05-18T19:37:02.133' AS DateTime))
INSERT [dbo].[reviews] ([review_id], [user_id], [product_id], [rating], [comment], [review_date]) VALUES (3, 3, 8, 5, N'Hoa quá đẹp luôn á shop', CAST(N'2026-05-18T22:24:19.893' AS DateTime))
INSERT [dbo].[reviews] ([review_id], [user_id], [product_id], [rating], [comment], [review_date]) VALUES (4, 11, 14, 5, N'quá oke', CAST(N'2026-05-19T20:16:26.107' AS DateTime))
INSERT [dbo].[reviews] ([review_id], [user_id], [product_id], [rating], [comment], [review_date]) VALUES (5, 11, 2, 5, N'quá đẹp', CAST(N'2026-05-20T21:17:47.557' AS DateTime))
SET IDENTITY_INSERT [dbo].[reviews] OFF
GO
INSERT [dbo].[roles] ([role_id], [role_name]) VALUES (1, N'ADMIN')
INSERT [dbo].[roles] ([role_id], [role_name]) VALUES (2, N'USER')
GO
SET IDENTITY_INSERT [dbo].[support_messages] ON 

INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (1, 3, N'hoan@gmail.com', N'client', N'hi', CAST(N'2026-05-17T21:28:26.993' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (2, 3, N'hoan@gmail.com', N'admin', N'chào', CAST(N'2026-05-17T21:39:27.003' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (3, 3, N'hoan@gmail.com', N'client', N'hi', CAST(N'2026-05-17T21:54:14.817' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (4, 3, N'hoan@gmail.com', N'client', N'hi', CAST(N'2026-05-17T21:56:53.987' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (5, 3, N'hoan@gmail.com', N'admin', N'chào', CAST(N'2026-05-17T21:59:06.337' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (6, 3, N'hoan@gmail.com', N'client', N'hi', CAST(N'2026-05-17T22:10:11.453' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (7, 3, N'hoan@gmail.com', N'admin', N'chào', CAST(N'2026-05-17T22:10:49.040' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (8, 1, N'admin@gmail.com', N'client', N'sff', CAST(N'2026-05-17T22:11:20.877' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (9, 3, N'hoan@gmail.com', N'client', N'hi shop', CAST(N'2026-05-18T19:32:00.933' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (10, 3, N'hoan@gmail.com', N'admin', N'chào bạn', CAST(N'2026-05-18T19:32:10.037' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (11, 3, N'hoan@gmail.com', N'client', N'shop bán hoa xuyến chi không', CAST(N'2026-05-18T19:32:26.617' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (12, 3, N'hoan@gmail.com', N'admin', N'hoa cứt lợn à bạn', CAST(N'2026-05-18T19:32:38.627' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (13, 3, N'hoan@gmail.com', N'client', N'đúng rồi shop', CAST(N'2026-05-18T19:32:48.850' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (14, 3, N'hoan@gmail.com', N'client', N'bán cho mình 2 bó nhé', CAST(N'2026-05-18T19:32:56.827' AS DateTime), NULL)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (15, 11, N'hoan1@gmail.com', N'client', N'hi shop', CAST(N'2026-05-20T21:18:29.643' AS DateTime), 0)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (16, 11, N'hoan1@gmail.com', N'admin', N'chào bạn', CAST(N'2026-05-20T21:18:59.500' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (17, 11, N'hoan1@gmail.com', N'client', N'oke shop', CAST(N'2026-05-20T21:19:12.007' AS DateTime), 0)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (18, 11, N'hoan1@gmail.com', N'admin', N'oke bạn', CAST(N'2026-05-20T21:19:23.040' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (19, 11, N'hoan1@gmail.com', N'admin', N'Chúc mừng! Đơn hàng của bạn đạt trên 3.000.000đ. Bạn nhận được mã giảm giá 10% cho lần mua tiếp theo. Mã của bạn là: VIP3M-63629', CAST(N'2026-05-20T21:20:35.780' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (20, 11, N'hoan1@gmail.com', N'client', N'hi', CAST(N'2026-05-21T08:02:31.967' AS DateTime), 0)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (21, 11, N'hoan1@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: FLOWER30 giảm 30%!', CAST(N'2026-05-21T08:54:33.733' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (22, 6, N'hoan2@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: FLOWER30 giảm 30%!', CAST(N'2026-05-21T08:54:33.813' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (23, 3, N'hoan@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: FLOWER30 giảm 30%!', CAST(N'2026-05-21T08:54:33.843' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (24, 2, N'long@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: FLOWER30 giảm 30%!', CAST(N'2026-05-21T08:54:33.863' AS DateTime), 0)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (25, 11, N'hoan1@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: BAHOA10 giảm 10%!', CAST(N'2026-05-21T09:13:38.873' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (26, 6, N'hoan2@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: BAHOA10 giảm 10%!', CAST(N'2026-05-21T09:13:38.890' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (27, 3, N'hoan@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: BAHOA10 giảm 10%!', CAST(N'2026-05-21T09:13:38.963' AS DateTime), 1)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (28, 2, N'long@gmail.com', N'admin', N'🎁 TẶNG BẠN MÃ GIẢM GIÁ: BAHOA10 giảm 10%!', CAST(N'2026-05-21T09:13:39.040' AS DateTime), 0)
INSERT [dbo].[support_messages] ([msg_id], [client_id], [client_email], [sender_role], [message_text], [created_at], [is_read]) VALUES (29, 11, N'hoan1@gmail.com', N'admin', N'chào bạn', CAST(N'2026-05-21T13:01:55.830' AS DateTime), 1)
SET IDENTITY_INSERT [dbo].[support_messages] OFF
GO
SET IDENTITY_INSERT [dbo].[users] ON 

INSERT [dbo].[users] ([user_id], [fullname], [email], [password], [phone], [address], [security_question], [security_answer], [role_id], [created_at]) VALUES (1, N'Admin', N'admin@gmail.com', N'123456', N'0123456789', N'Ha Noi', N'Loài hoa bạn yêu thích nhất là gì?', N'Hoa hồng', 1, CAST(N'2026-05-17T13:29:56.643' AS DateTime))
INSERT [dbo].[users] ([user_id], [fullname], [email], [password], [phone], [address], [security_question], [security_answer], [role_id], [created_at]) VALUES (2, N'Long Vu', N'long@gmail.com', N'123456', N'0988888888', N'Ho Chi Minh', N'Loài hoa bạn yêu thích nhất là gì?', N'Hoa hồng', 2, CAST(N'2026-05-17T13:29:56.660' AS DateTime))
INSERT [dbo].[users] ([user_id], [fullname], [email], [password], [phone], [address], [security_question], [security_answer], [role_id], [created_at]) VALUES (3, N'Đỗ Sỹ Hoàn', N'hoan@gmail.com', N'12345', N'0900000029', N'Phúc Thọ', N'Tên thú cưng đầu tiên của bạn là gì?', N'Chó', 2, CAST(N'2026-05-17T13:31:03.000' AS DateTime))
INSERT [dbo].[users] ([user_id], [fullname], [email], [password], [phone], [address], [security_question], [security_answer], [role_id], [created_at]) VALUES (6, N'Đỗ Hoàn', N'hoan2@gmail.com', N'123456', N'0900000346', N'Hải Phòng', N'Thành phố bạn sinh ra ở đâu?', N'Phúc Thọ', 2, CAST(N'2026-05-19T20:07:43.597' AS DateTime))
INSERT [dbo].[users] ([user_id], [fullname], [email], [password], [phone], [address], [security_question], [security_answer], [role_id], [created_at]) VALUES (11, N'Sỹ Hoàn', N'hoan1@gmail.com', N'12345', N'', N'Phúc Thọ', N'Tên người bạn thân nhất thời thơ ấu?', N'quá oke', 2, CAST(N'2026-05-19T20:13:26.320' AS DateTime))
SET IDENTITY_INSERT [dbo].[users] OFF
GO
/****** Object:  Index [UQ__cart__B9BE370E10AB52F6]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[cart] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ__cart__B9BE370E818FD62E]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[cart] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ__cart__B9BE370E8CA21A12]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[cart] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ__cart__B9BE370EB8808D72]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[cart] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
/****** Object:  Index [UQ__cart__B9BE370ECDB6E237]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[cart] ADD UNIQUE NONCLUSTERED 
(
	[user_id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__coupons__357D4CF949496D52]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[coupons] ADD UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__coupons__357D4CF94A28CD49]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[coupons] ADD UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__coupons__357D4CF99631BDBB]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[coupons] ADD UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__coupons__357D4CF99C20781E]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[coupons] ADD UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__coupons__357D4CF9CE6B18A0]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[coupons] ADD UNIQUE NONCLUSTERED 
(
	[code] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E616402B1F7E5]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E616417121CDC]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E6164548EDCCD]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E61646161A996]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
SET ANSI_PADDING ON
GO
/****** Object:  Index [UQ__users__AB6E61646D6796C1]    Script Date: 6/19/2026 3:54:05 PM ******/
ALTER TABLE [dbo].[users] ADD UNIQUE NONCLUSTERED 
(
	[email] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
GO
ALTER TABLE [dbo].[cart_items] ADD  DEFAULT ((1)) FOR [quantity]
GO
ALTER TABLE [dbo].[coupon_usage] ADD  DEFAULT (getdate()) FOR [used_at]
GO
ALTER TABLE [dbo].[coupons] ADD  DEFAULT ((1)) FOR [status]
GO
ALTER TABLE [dbo].[coupons] ADD  DEFAULT ((0)) FOR [min_order_value]
GO
ALTER TABLE [dbo].[orders] ADD  DEFAULT (N'Pending') FOR [status]
GO
ALTER TABLE [dbo].[orders] ADD  DEFAULT (getdate()) FOR [order_date]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT ((0)) FOR [stock]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[products] ADD  DEFAULT ((0)) FOR [discount]
GO
ALTER TABLE [dbo].[reviews] ADD  DEFAULT (getdate()) FOR [review_date]
GO
ALTER TABLE [dbo].[support_messages] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[support_messages] ADD  DEFAULT ((0)) FOR [is_read]
GO
ALTER TABLE [dbo].[users] ADD  DEFAULT ((2)) FOR [role_id]
GO
ALTER TABLE [dbo].[users] ADD  DEFAULT (getdate()) FOR [created_at]
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[cart]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([cart_id])
REFERENCES [dbo].[cart] ([cart_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([cart_id])
REFERENCES [dbo].[cart] ([cart_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([cart_id])
REFERENCES [dbo].[cart] ([cart_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([cart_id])
REFERENCES [dbo].[cart] ([cart_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([cart_id])
REFERENCES [dbo].[cart] ([cart_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[cart_items]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([coupon_id])
REFERENCES [dbo].[coupons] ([coupon_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([coupon_id])
REFERENCES [dbo].[coupons] ([coupon_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([coupon_id])
REFERENCES [dbo].[coupons] ([coupon_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([coupon_id])
REFERENCES [dbo].[coupons] ([coupon_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([coupon_id])
REFERENCES [dbo].[coupons] ([coupon_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[coupon_usage]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([order_id])
REFERENCES [dbo].[orders] ([order_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[order_details]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[orders]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[categories] ([category_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[categories] ([category_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[categories] ([category_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[categories] ([category_id])
GO
ALTER TABLE [dbo].[products]  WITH CHECK ADD FOREIGN KEY([category_id])
REFERENCES [dbo].[categories] ([category_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([product_id])
REFERENCES [dbo].[products] ([product_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[reviews]  WITH CHECK ADD FOREIGN KEY([user_id])
REFERENCES [dbo].[users] ([user_id])
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
ALTER TABLE [dbo].[users]  WITH CHECK ADD FOREIGN KEY([role_id])
REFERENCES [dbo].[roles] ([role_id])
GO
USE [master]
GO
ALTER DATABASE [wedbanhoa] SET  READ_WRITE 
GO
select * from categories