import React, { useRef, useMemo } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import * as THREE from 'three';

const PieSlice = ({ startAngle, endAngle, radius, height, color, label }) => {
  useMemo(() => {
    const shape = new THREE.Shape();
    shape.moveTo(0, 0);
    const isFullCircle = Math.abs(endAngle - startAngle) >= Math.PI * 2 - 0.01;
    const effectiveEnd = isFullCircle ? endAngle + 0.001 : endAngle;
    shape.absarc(0, 0, radius, startAngle, effectiveEnd, false);
    shape.lineTo(0, 0);
    return shape;
  }, [startAngle, endAngle, radius]);

  const geometry = useMemo(() => {
    const isFullCircle = Math.abs(endAngle - startAngle) >= Math.PI * 2 - 0.01;

    if (isFullCircle) {
      const geo = new THREE.CylinderGeometry(radius, radius, height, 64, 1, false);
      geo.rotateX(Math.PI / 2); // match the rotation applied to other slices
      return geo;
    }

    const shape = new THREE.Shape();
    shape.moveTo(0, 0);
    shape.absarc(0, 0, radius, startAngle, endAngle, false);
    shape.lineTo(0, 0);

    return new THREE.ExtrudeGeometry(shape, {
      steps: 1,
      depth: height,
      bevelEnabled: false,
    });
  }, [startAngle, endAngle, radius, height]);


  const centerAngle = (startAngle + endAngle) / 2;
  const labelX = (radius / 1.5) * Math.sin(centerAngle);
  const labelY = height + 0.2;
  const labelZ = (radius / 1.5) * Math.cos(centerAngle);

  return (
    <group rotation={[-Math.PI / 2, 0, 0]}>
      <mesh geometry={geometry}>
        <meshBasicMaterial color={color} toneMapped={false} />
      </mesh>

      <Html position={[labelX, labelY, labelZ]} center style={{ pointerEvents: 'none' }} zIndexRange={[0, 0]}>
        <div
          style={{
            color,
            textShadow: '0 0 10px rgba(0, 0, 0, 0.8)',
            fontSize: '14px',
            letterSpacing: '1.5px',
            background: 'transparent',
            padding: '4px 8px',
            borderRadius: '8px',
            whiteSpace: 'nowrap'
          }}
        >
          {label}
        </div>
      </Html>
    </group>
  );
};

const ChartScene = ({ data }) => {
  const groupRef = useRef();

  const total = useMemo(() => data.reduce((acc, item) => acc + item.value, 0), [data]);
  const colors = useMemo(() => ['#f72585', '#ffaf40', '#7209b7', '#3a0ca3', '#4361ee', '#4cc9f0'], []);

  const slices = useMemo(() => {
    const nonZeroData = data.filter(item => item.value > 0);
    const isOnlySlice = nonZeroData.length === 1;

    let startAngle = 0;

    return nonZeroData.map((item, i) => {
      const angle = isOnlySlice ? Math.PI * 2 : (item.value / total) * Math.PI * 2;
      const endAngle = startAngle + angle;
      const slice = {
        startAngle,
        endAngle,
        color: colors[i % colors.length],
        label: `${item.name}: ${item.value}`,
      };
      startAngle = endAngle;
      return slice;
    });
  }, [data, total, colors]);

  useFrame(() => {
    if (groupRef.current) {
      groupRef.current.rotation.y += 0.001;
    }
  });

  return (
    <>
      <ambientLight intensity={0.6} />
      <directionalLight position={[5, 10, 7]} intensity={1} castShadow />
      <group ref={groupRef} position={[0, -1.5, 0]}>
        {slices.map((slice, index) => (
          <PieSlice
            key={index}
            startAngle={slice.startAngle}
            endAngle={slice.endAngle}
            radius={3}
            height={1.2}
            color={slice.color}
            label={slice.label}
          />
        ))}
      </group>
      <OrbitControls enableZoom />
    </>
  );
};


const PieChart3D = ({ data }) => {
  return (
    <Canvas camera={{ position: [0, 4, 10], fov: 50 }}>
      <ChartScene data={data} />
    </Canvas>
  );
};

export default PieChart3D;
