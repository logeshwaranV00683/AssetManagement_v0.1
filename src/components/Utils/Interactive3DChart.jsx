import React, { useRef, useMemo, useEffect } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import * as THREE from 'three';

const PieSlice = ({ startAngle, endAngle, radius, height, color, label, total, index }) => {
  const meshRef = useRef();

  const shape = useMemo(() => {
    const shape = new THREE.Shape();
    shape.moveTo(0, 0);
    const epsilon = 0.001;
    shape.absarc(0, 0, radius + epsilon, startAngle, endAngle + epsilon, false);
    shape.lineTo(0, 0);
    return shape;
  }, [startAngle, endAngle, radius]);

  const geometry = useMemo(() => {
    const extrudeSettings = {
      steps: 1,
      depth: height,
      bevelEnabled: false,
    };
    return new THREE.ExtrudeGeometry(shape, extrudeSettings);
  }, [shape, height]);


  const centerAngle = (startAngle + endAngle) / 2;
  const labelX = (radius / 1.5) * Math.cos(centerAngle);
  const labelY = height + 0.2;
  const labelZ = (radius / 1.5) * Math.sin(centerAngle);

  useFrame(() => {
    if (meshRef.current) {
      meshRef.current.rotation.y += 0.001;
    }
  });

  return (
    <group rotation={[-Math.PI / 2, 0, 0]}> {/* Rotate to stand vertically */}
      <mesh ref={meshRef} geometry={geometry} >
        <meshStandardMaterial
          color={color}
          emissiveIntensity={0.4}
          roughness={0.4}
          metalness={0.1}
        />
      </mesh>

      <Html position={[labelX, labelY, labelZ]} center style={{ pointerEvents: 'none' }} zIndexRange={[0, 0]}  >
        <div
          style={{
            color: color,
            textShadow: '0 0 3px rgba(0,0,0,0.6)',
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

const PieChart3D = ({ data }) => {
  const total = useMemo(() => data.reduce((acc, item) => acc + item.value, 0), [data]);
  const colors = useMemo(() => ['#f72585', '#ffaf40', '#7209b7', '#3a0ca3', '#4361ee', '#4cc9f0'], []);

  const slices = useMemo(() => {
    let startAngle = 0;
    return data.map((item, i) => {
      const angle = (item.value / total) * Math.PI * 2;
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


  return (
    <Canvas camera={{ position: [0, 4, 10], fov: 50 }}>
      <ambientLight intensity={0.6} />
      <directionalLight position={[5, 10, 7]} intensity={1} castShadow />
      <group position={[0, -1.5, 0]}>
        {slices.map((slice, index) => (
          <PieSlice
            key={index}
            index={index}
            startAngle={slice.startAngle}
            endAngle={slice.endAngle}
            radius={3}
            height={1.2}
            color={slice.color}
            label={slice.label}
            total={total}
          />
        ))}
      </group>
      <OrbitControls enableZoom={true} />
    </Canvas>
  );
};

export default PieChart3D;