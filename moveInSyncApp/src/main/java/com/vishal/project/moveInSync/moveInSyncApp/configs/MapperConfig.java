package com.vishal.project.moveInSync.moveInSyncApp.configs;


import com.vishal.project.moveInSync.moveInSyncApp.dto.PointDTO;
import com.vishal.project.moveInSync.moveInSyncApp.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class  MapperConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper mapper = new ModelMapper();
        mapper.typeMap(PointDTO.class, Point.class).setConverter(converter -> {
            PointDTO pointDTO = converter.getSource();
return GeometryUtil.createPoint(pointDTO);
        });
mapper.typeMap(Point.class, PointDTO.class).setConverter(context -> {
    Point point = context.getSource();
    double coordinates[] = {
            point.getX(),
            point.getY()
    };

    return new PointDTO(coordinates);
});
        return mapper;
    }
}