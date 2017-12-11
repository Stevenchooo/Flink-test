
       <FileToHDFS action="ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM">
        <!-- 文件配置信息 -->
            <Fileconf>Video</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM/*_ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM_*000000.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_MOVIE_BASE_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
       <CompressType>orc_zlib</CompressType>
</FileToHDFS>


       <FileToHDFS action="ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM">
        <!-- 文件配置信息 -->
            <Fileconf>Video</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM/*_ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM_*000000.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_MOVIE_BASE_CP_INFO_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
       <CompressType>orc_zlib</CompressType>
</FileToHDFS>




       <FileToHDFS action="ODS_VIDEO_CLOUD_MOVIE_VOLUME_DM">
        <!-- 文件配置信息 -->
            <Fileconf>Video</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_MOVIE_VOLUME_DM/*_ODS_VIDEO_CLOUD_MOVIE_VOLUME_DM_*000000.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_MOVIE_VOLUME_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_MOVIE_VOLUME_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
       <CompressType>orc_zlib</CompressType>
</FileToHDFS>



       <FileToHDFS action="ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM">
        <!-- 文件配置信息 -->
            <Fileconf>Video</Fileconf>
            <!-- 输入文件配置-->
            <MultiInputFileConf>
                <!-- 输入文件列表-->
                <InputFileList>/MFS/DataIn/Communicate/odsdata/ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM/*_ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM_*000000.txt.lzo</InputFileList>
                <!-- 输入文件个数  如果配置成-1，该参数无效，只考虑等待时间 -->
                <InputFileMinCount>1</InputFileMinCount>
                <!-- 等待输入时间（分钟） -->
                <WaitInputMinutes>120</WaitInputMinutes>
                <!-- 文件字段的分割符，默认使用\\0x01 -->
                <Separator>\001</Separator>
            </MultiInputFileConf>
            <!-- 表名 -->
            <Tablename>ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM</Tablename>
            <!-- 表存储位置 -->
            <Tablelocation>VIDEO/ODS_VIDEO_CLOUD_MOVIE_VOLUME_SOURCE_DM</Tablelocation>
            <!-- 分区 none pt_y pt_m pt_d pt_h pt_min -->
            <Partition>pt_d</Partition>
       <CompressType>orc_zlib</CompressType>
</FileToHDFS>





