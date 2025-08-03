import React, { useRef, useMemo, useEffect } from 'react';
import { Canvas, useFrame } from '@react-three/fiber';
import { OrbitControls, Html } from '@react-three/drei';
import { useSpring, a } from '@react-spring/three';

const ChartBar = ({ height, color, label, position }) => {
  const meshRef = useRef();
  const groupRef = useRef();
  const labelRef = useRef();
  const floatStart = useRef(Math.random() * 1000);

  const { scaleY } = useSpring({
    from: { scaleY: 0 },
    to: { scaleY: height },
    config: { mass: 1, tension: 120, friction: 20 },
  });

  useFrame((state) => {
    const elapsed = state.clock.getElapsedTime();
    const floatY = Math.sin(elapsed + floatStart.current) * 0.1;

    if (groupRef.current) {
      groupRef.current.position.y = position[1] - 1.2 + floatY;
    }

    if (meshRef.current) {
      meshRef.current.rotation.y += 0.003;
    }

    if (labelRef.current) {
      labelRef.current.style.transform = `translateY(${5 + floatY * 10}px)`;
    }
  });

  return (
    <group ref={groupRef} position={[position[0], position[1] - 1.2, position[2]]}>
      <a.mesh
        ref={meshRef}
        position-y={scaleY.to(h => h / 2)}
        scale-y={scaleY}
      >
        <boxGeometry args={[1, 1, 1]} />
        <meshStandardMaterial color={color} />
      </a.mesh>

      <Html position={[0, height + 0.5, 0]} center>
        <div
          ref={labelRef}
          style={{
            color: '#00f0ff',
            fontWeight: 550,
            fontSize: '16px',
            transition: 'opacity 1s',
            animation: 'riseFadeIn 1s ease-out forwards',
            transform: 'translateY(5px)',
            willChange: 'transform',
          }}
        >
          {label}
        </div>
      </Html>
    </group>
  );
};

const riseFadeInStyle = `
@keyframes riseFadeIn {
  from { transform: translateY(10px); opacity: 0; }
  to { transform: translateY(0px); opacity: 1; }
}
`;

const ChartScene = ({ data }) => {
  useEffect(() => {
    const style = document.createElement("style");
    style.innerHTML = riseFadeInStyle;
    document.head.appendChild(style);
    return () => document.head.removeChild(style);
  }, []);

  const spacing = 2;
  const colors = useMemo(() => ['#00e0ff', '#f72585', '#ffd166'], []);

  const bars = useMemo(() => {
    return data.map((d, i) => ({
      ...d,
      position: [i * spacing - spacing, 0, 0],
      color: colors[i % colors.length],
    }));
  }, [data, colors]);

  return (
    <Canvas camera={{ position: [0, 5, 10], fov: 25 }} shadows>
      <ambientLight intensity={0.5} />
      <spotLight position={[10, 15, 10]} angle={0.3} penumbra={1} castShadow />

      {bars.map((bar, idx) => (
        <ChartBar
          key={idx}
          height={bar.value / 10}
          color={bar.color}
          label={`${bar.name}: ${bar.value}`}
          position={bar.position}
        />
      ))}

      <OrbitControls enableZoom={true} />
    </Canvas>
  );
};

export default ChartScene;
